package com.bean.kafka;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 16:53
 * @Description:
 */
public class KafkaOffsetMonitorUrl {
    //topic详细
    String topicdetails = "/topicdetails/";
    //group
    String group = "/group/";
    //topic列表
    String topiclist="/topiclist";

    public String getTopicdetails() {
        return topicdetails;
    }

    public void setTopicdetails(String topicdetails) {
        this.topicdetails = topicdetails;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopiclist() {
        return topiclist;
    }

    public void setTopiclist(String topiclist) {
        this.topiclist = topiclist;
    }
}
