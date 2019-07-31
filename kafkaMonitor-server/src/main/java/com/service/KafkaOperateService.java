package com.service;

import com.bean.kafka.TopicInfo;
import com.bean.kafka.TopicPartitionInfoBean;
import com.bean.user.UserInfo;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/30 11:02
 * @Description: kafka操作API
 */
public interface KafkaOperateService {
    /**
     * 创建topic，
     *
     * @param topicName
     * @param partitions
     * @param replication
     * @return
     */
    boolean createTopic(String zookeeper, String topicName, int partitions, int replication, String createBy);

    /**
     * 删除topic
     *
     * @param zookeeper
     * @param topicName
     * @return
     */
    boolean deleteTopic(String zookeeper, String topicName, String createBy);


    /**
     * 获取topic列表
     *
     * @param zookeeper
     * @param userInfo
     * @return
     */
    List<TopicInfo> getTopicList(String zookeeper, UserInfo userInfo);

    /**
     * 判断topic是否存在
     *
     * @param zookeeper
     * @param topicName
     * @return
     */
    boolean topicExists(String zookeeper, String topicName);

    /**
     * 获取topic的分区详情数据
     *
     * @param zookeeper
     * @param topic
     * @return
     */
    List<TopicPartitionInfoBean> getTopicPartitionDetail(String zookeeper, String topic);
}
