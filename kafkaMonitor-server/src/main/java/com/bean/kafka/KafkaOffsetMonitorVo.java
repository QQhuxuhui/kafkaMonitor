package com.bean.kafka;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/4 12:21
 * @Description:
 */
public class KafkaOffsetMonitorVo {

    List<String> emails;

    KafkaOffsetMonitor kafkaOffsetMonitor;

    public KafkaOffsetMonitor getKafkaOffsetMonitor() {
        return kafkaOffsetMonitor;
    }

    public void setKafkaOffsetMonitor(KafkaOffsetMonitor kafkaOffsetMonitor) {
        this.kafkaOffsetMonitor = kafkaOffsetMonitor;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
