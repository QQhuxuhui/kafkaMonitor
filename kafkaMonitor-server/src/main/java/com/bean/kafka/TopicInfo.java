package com.bean.kafka;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 16:58
 * @Description:
 */
@Entity
public class TopicInfo {

    @Id
    String id;
    String topic;
    @Column(columnDefinition = "varchar(64) comment '创建人'")
    String createBy;
    @Column(columnDefinition = "datetime NULL  comment '创建日期'")
    Date createDate;
    @Column(columnDefinition = "timestamp not NULL default CURRENT_TIMESTAMP comment '更新时间'")
    Date updateDate;
    String brokers;

    //是否删除记录
    Boolean deleted = false;

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }
}
