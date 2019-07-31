package com.bean.kafka;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/22 16:23
 * @Description:
 */
public class KafkaConsumerConfig {

    public KafkaConsumerConfig() {
    }

    public KafkaConsumerConfig(String zookeeperConn) {
        this.zookeeperConn = zookeeperConn;
    }

    String zookeeperConn;

    public String getZookeeperConn() {
        return zookeeperConn;
    }

    public void setZookeeperConn(String zookeeperConn) {
        this.zookeeperConn = zookeeperConn;
    }
}
