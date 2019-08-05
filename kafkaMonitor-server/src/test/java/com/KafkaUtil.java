package com;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: huxuhui
 * @Date: 2019/8/2 17:33
 * @Description:
 */
public class KafkaUtil {
    private static Logger logger = LoggerFactory.getLogger(KafkaUtil.class);
    private static final int ZOOKEEPER_TIMEOUT = 30000;
    private final CountDownLatch latch = new CountDownLatch(1);

    public ZooKeeper getZookeeper(String connectionString) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(connectionString, ZOOKEEPER_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return zk;
    }

    public static Properties getConsumerProperties(String groupId, String bootstrap_servers) {
        Properties props = new Properties();
        props.put("group.id", groupId);
        props.put("bootstrap.servers", bootstrap_servers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    /**
     * 获取logSize, offset, lag等信息
     * @param zk
     * @param bootstrap_servers
     * @param groupId
     * @param topics null查询groupId消费过的所有topic
     * @param sorted
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getLagByGroupAndTopic(ZooKeeper zk, String bootstrap_servers, String groupId,
                                                           String[] topics, boolean sorted) throws Exception {

        List<Map<String, Object>> topicPatitionMapList = new ArrayList<>();

        // 获取group消费过的所有topic
        List<String> topicList = null;
        if (topics == null || topics.length == 0) {
            try {
                topicList = zk.getChildren("/consumers/" + groupId + "/offsets", false);
            } catch (KeeperException | InterruptedException e) {
                logger.error("从zookeeper获取topics失败：zkState: {}, groupId:{}", zk.getState(), groupId);
                throw new Exception("从zookeeper中获取topics失败");
            }
        } else {
            topicList = Arrays.asList(topics);
        }

        Properties consumeProps = getConsumerProperties(groupId, bootstrap_servers);
        logger.info("consumer properties:{}", consumeProps);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumeProps);
        Map<String, List<PartitionInfo>> listTopics = consumer.listTopics();

        // 查询topic partitions
        for (String topic : topicList) {
            List<PartitionInfo> partitionsFor = consumer.partitionsFor(topic);
            //由于有时延， 尽量逐个topic查询， 减少lag为负数的情况
            List<TopicPartition> topicPartitions = new ArrayList<>();

            // 获取topic对应的 TopicPartition
            for (PartitionInfo partitionInfo : partitionsFor) {
                TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                topicPartitions.add(topicPartition);
            }
            // 查询logSize
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
            for (Entry<TopicPartition, Long> entry : endOffsets.entrySet()) {
                TopicPartition partitionInfo = entry.getKey();
                // 获取offset
                String offsetPath = MessageFormat.format("/consumers/{0}/offsets/{1}/{2}", groupId, partitionInfo.topic(),
                        partitionInfo.partition());
                byte[] data = zk.getData(offsetPath, false, null);
                long offset = Long.valueOf(new String(data));

                Map<String, Object> topicPatitionMap = new HashMap<>();
                topicPatitionMap.put("group", groupId);
                topicPatitionMap.put("topic", partitionInfo.topic());
                topicPatitionMap.put("partition", partitionInfo.partition());
                topicPatitionMap.put("logSize", endOffsets.get(partitionInfo));
                topicPatitionMap.put("offset", offset);
                topicPatitionMap.put("lag", endOffsets.get(partitionInfo) - offset);
                topicPatitionMapList.add(topicPatitionMap);
            }
        }
        consumer.close();

//        if(sorted) {
//            Collections.sort(topicPatitionMapList, new Comparator<Map<String,Object>>() {
//                @Override
//                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                    if(o1.get("topic").equals(o2.get("topic"))) {
//                        return ((Integer)o1.get("partition")).compareTo((Integer)o2.get("partition"));
//                    }
//                    return ((String)o1.get("topic")).compareTo((String)o2.get("topic"));
//                }
//            });
//        }

        return topicPatitionMapList;
    }

    public static void main(String[] args) throws Exception {
        String bootstrap_servers = "10.80.0.73:6667";
//        String groupId = "XGIT_MOVING_XCMG_RAWDATA_HISTORY_1_xgit_xcmg_xgcy_data";
        String groupId = "bbb";
        String[] topics = null;//{"test1", "test2", test3};

        KafkaUtil kafkaUtil = new KafkaUtil();
        String connectionString = "xcmg55:2181";
        ZooKeeper zk = kafkaUtil.getZookeeper(connectionString);
        if (zk == null) {
            throw new RuntimeException("获取zookeeper连接失败");
        }
        List<Map<String, Object>> topicPatitionMapList = kafkaUtil.getLagByGroupAndTopic(zk, bootstrap_servers,
                groupId, topics, true);

        for (Map<String, Object> map : topicPatitionMapList) {
            System.out.println(map);
        }
        zk.close();
    }
}
