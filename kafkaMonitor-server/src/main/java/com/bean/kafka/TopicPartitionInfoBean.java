package com.bean.kafka;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 15:52
 * @Description: topic分区的详情数据
 */
public class TopicPartitionInfoBean {
    //topic
    String topic;
    //分区号
    int partition;
    //副本分部的broker的地址
    List<String> address;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
