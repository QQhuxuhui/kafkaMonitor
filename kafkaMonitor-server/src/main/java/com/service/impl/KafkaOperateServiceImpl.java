package com.service.impl;

import com.bean.kafka.TopicInfo;
import com.bean.kafka.TopicPartitionInfoBean;
import com.bean.user.UserInfo;
import com.common.utils.MD5Util;
import com.dao.jpa.TopicInfoRepository;
import com.service.KafkaOperateService;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.cluster.Broker;
import kafka.cluster.EndPoint;
import kafka.common.TopicAndPartition;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.immutable.Vector;

import java.util.*;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/30 11:45
 * @Description:
 */
@Service
public class KafkaOperateServiceImpl implements KafkaOperateService {

    @Autowired
    private TopicInfoRepository topicInfoRepository;

    @Override
    public boolean createTopic(String zookeeper, String topicName, int partitions, int replication, String createBy) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 30000, 30000, JaasUtils.isZkSecurityEnabled());
            AdminUtils.createTopic(zkUtils, topicName, partitions, replication, new Properties(), RackAwareMode.Enforced$.MODULE$);
            zkUtils.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        //创建完成，入库
        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setId(MD5Util.GetMD5Code(topicName));
        topicInfo.setCreateBy(createBy);
        topicInfo.setTopic(topicName);
        topicInfoRepository.save(topicInfo);
        return true;
    }

    @Override
    public boolean deleteTopic(String zookeeper, String topicName, String createBy) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 30000, 30000, JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils, topicName);
        } catch (Exception e) {
            throw e;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        //删除，修改库中的数据状态
        TopicInfo topicInfo = topicInfoRepository.getOne(MD5Util.GetMD5Code(topicName));
        topicInfo.setDeleted(true);
        topicInfo.setUpdateDate(new Date());
        topicInfo.setCreateBy(createBy);
        topicInfoRepository.save(topicInfo);
        return true;
    }

    @Override
    public List<TopicInfo> getTopicList(String zookeeper, UserInfo userInfo) {
        ZkUtils zkUtils = null;
        List<String> list;
        List<TopicInfo> returnInfo = new ArrayList<>();
        List<TopicInfo> saveInfo = new ArrayList<>();
        List<String> brokerList;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 30000, 30000, JaasUtils.isZkSecurityEnabled());
            //获取brokers
            brokerList = getBrokersAddress(zkUtils);
            list = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
            for (String topic : list) {
                TopicInfo topicInfo = topicInfoRepository.findById(getId(topic));
                if (topicInfo == null) {
                    topicInfo = new TopicInfo();
                    topicInfo.setId(MD5Util.GetMD5Code(topic));
                    topicInfo.setCreateBy(userInfo.getName());
                    topicInfo.setUpdateDate(new Date());
                    topicInfo.setTopic(topic);
                    topicInfo.setBrokers(brokerList.toString().replaceAll("\\[|\\]", ""));
                    saveInfo.add(topicInfo);
                }
                returnInfo.add(topicInfo);
            }
            if (saveInfo.size() > 0) {
                topicInfoRepository.save(saveInfo);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        return returnInfo;
    }

    @Override
    public List<TopicPartitionInfoBean> getTopicPartitionDetail(String zookeeper, String topic) {
        List<TopicPartitionInfoBean> topicPartitionInfoBeans = new ArrayList<>();
        ZkUtils zkUtils = null;
        List<String> list = new ArrayList<>();
        list.add(topic);
        Map<TopicAndPartition, Seq<Object>> map = null;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 30000, 30000, JaasUtils.isZkSecurityEnabled());

            map = JavaConversions.mapAsJavaMap(zkUtils.getReplicaAssignmentForTopics(JavaConverters.asScalaIteratorConverter(list.iterator()).asScala().toSeq()));
            //获取分区数据
            for (TopicAndPartition topicAndPartition : map.keySet()) {
                List<String> addressList = new ArrayList<>();
                Vector vector = (Vector) map.get(topicAndPartition);
                List<Integer> brokerIds = JavaConverters.seqAsJavaList(vector);
                //通过brokerId获取服务地址信息
                for (Integer brokerId : brokerIds) {
                    Option<Broker> brokers = zkUtils.getBrokerInfo(brokerId);
                    if (brokers.iterator().hasNext()) {
                        Broker broker = brokers.iterator().next();
                        EndPoint endPoint = JavaConverters.seqAsJavaList(broker.endPoints()).get(0);
                        String address = endPoint.host() + ":" + endPoint.port();
                        addressList.add(address);
                    }
                }
                TopicPartitionInfoBean topicPartitionInfoBean = new TopicPartitionInfoBean();
                topicPartitionInfoBean.setTopic(topic);
                topicPartitionInfoBean.setPartition(topicAndPartition.partition());
                topicPartitionInfoBean.setAddress(addressList);
                topicPartitionInfoBeans.add(topicPartitionInfoBean);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        return topicPartitionInfoBeans;

    }

    @Override
    public boolean topicExists(String zookeeper, String topicName) {
        ZkUtils zkUtils = null;
        boolean exist;
        try {
            zkUtils = ZkUtils.apply(zookeeper, 30000, 30000, JaasUtils.isZkSecurityEnabled());
            exist = AdminUtils.topicExists(zkUtils, topicName);
        } catch (Exception e) {
            throw e;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        return exist;
    }

    private List<String> getBrokersAddress(ZkUtils zkUtils) {
        List<String> brokersList = new ArrayList<>();
        //获取brokers
        JavaConverters.seqAsJavaList(zkUtils.getAllBrokersInCluster());
        for (Broker broker : JavaConverters.seqAsJavaList(zkUtils.getAllBrokersInCluster())) {
            Option<Broker> brokers = zkUtils.getBrokerInfo(broker.id());
            if (brokers.iterator().hasNext()) {
                Broker broker1 = brokers.iterator().next();
                EndPoint endPoint = JavaConverters.seqAsJavaList(broker1.endPoints()).get(0);
                String address = endPoint.host() + ":" + endPoint.port();
                brokersList.add(address);
            }
        }
        return brokersList;
    }


    private String getId(String topic) {
        return MD5Util.GetMD5Code(topic);
    }

}
