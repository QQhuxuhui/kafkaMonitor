package com.bean.kafka;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/9 18:25
 * @Description:
 */
public class GroupOffsetAndLagBean {

    String address;
    //监控的topic
    String topic;
    //监控的groupId
    String group;
    //阈值
    Integer lag;
    //当前偏移量
    Long offset;



    public String getTopic() {
        return topic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
