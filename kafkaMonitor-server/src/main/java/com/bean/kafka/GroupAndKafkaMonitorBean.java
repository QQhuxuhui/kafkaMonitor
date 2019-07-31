package com.bean.kafka;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/12 18:02
 * @Description: 用于装载监控的数据和偏移量的数据
 */
public class GroupAndKafkaMonitorBean {

    KafkaOffsetMonitor kafkaOffsetMonitor;
    Group group;
    String address;

    public KafkaOffsetMonitor getKafkaOffsetMonitor() {
        return kafkaOffsetMonitor;
    }

    public void setKafkaOffsetMonitor(KafkaOffsetMonitor kafkaOffsetMonitor) {
        this.kafkaOffsetMonitor = kafkaOffsetMonitor;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
