package com.bean.kafka;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 18:53
 * @Description: kafkaoffsetmonitor服务配置
 */
@Entity
public class KafkaOffsetMonitorManager {
    @Id
    @Column(columnDefinition = "varchar(32) comment '主键，UUID'")
    String id;
    @Column(columnDefinition = "varchar(30) comment '服务地址'")
    String address;
    @Column(columnDefinition = "varchar(20) comment '服务名称（自定义）'")
    String name;

    @Column(columnDefinition = "timestamp NULL default CURRENT_TIMESTAMP comment '创建时间'")
    Date createDate;

    Date updateDate;
    @Column(columnDefinition = "boolean default false comment '是否删除标志,默认false(0)，未删除'")
    Boolean deleted;
    //标志位
    @Column(columnDefinition = "varchar(10) default null comment '标志位'")
    String tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
