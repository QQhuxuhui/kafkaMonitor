package com.bean.kafka;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 15:37
 * @Description: kafka偏移量监控实体类
 */
@Entity
public class KafkaOffsetMonitor {
    @Id
    @Column(columnDefinition = "varchar(32) comment '主键，由address、topic、groupId'")
    String id;
    @Column(columnDefinition = "varchar(64) comment 'topic'")
    String topic;
    @Column(columnDefinition = "varchar(64) comment '服务地址'")
    String address;
    @Column(columnDefinition = "varchar(64) comment 'groupId'")
    String groupId;
    //分区
    @Column(columnDefinition = "int comment 'partition分区'")
    Integer part;
    //监控阈值，大于阈值报警
    @Column(columnDefinition = "int comment '监控阈值，大于阈值报警'")
    Integer lag;
    @Column(columnDefinition = "varchar(64) comment '创建人'")
    String createBy;
    @Column(columnDefinition = "datetime NULL comment '创建日期'")
    Date createDate;
    @Column(columnDefinition = "timestamp not NULL default CURRENT_TIMESTAMP comment '更新时间'")
    Date updateDate;
    //是否开启邮件提醒
    Boolean emailAlter;
    //是否删除记录
    Boolean deleted = false;

    //备注
    @Column(columnDefinition = "varchar(128) comment '备注描述'")
    String description;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public Integer getLag() {
        return lag;
    }

    public void setLag(Integer lag) {
        this.lag = lag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getEmailAlter() {
        return emailAlter;
    }

    public void setEmailAlter(Boolean emailAlter) {
        this.emailAlter = emailAlter;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
