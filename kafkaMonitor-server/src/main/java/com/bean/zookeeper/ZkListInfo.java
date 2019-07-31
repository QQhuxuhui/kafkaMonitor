package com.bean.zookeeper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/7 09:54
 * @Description: zookeeper list info
 */
@Entity
public class ZkListInfo {
    @Id
    String id;
    String name;
    String zookeeperList;
    @Column(columnDefinition = "varchar(64) comment '创建人'")
    String createBy;
    @Column(columnDefinition = "timestamp NULL comment '创建日期'")
    Date createDate;
    @Column(columnDefinition = "timestamp not NULL default CURRENT_TIMESTAMP comment '更新时间'")
    Date updateDate;
    //是否删除记录
    Boolean deleted = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZookeeperList() {
        return zookeeperList;
    }

    public void setZookeeperList(String zookeeperList) {
        this.zookeeperList = zookeeperList;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
}
