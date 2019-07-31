package com.bean.shell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/28 16:21
 * @Description:
 */
@Entity
public class ServerInfoBean {

    @Id
    String id;
    //用户名
    String username = "root";
    //密码
    String password = "XreaServer_2014";
    //ip
    String ip;

    int port = 22;

    @Column(columnDefinition = "varchar(64) comment '创建人'")
    String createBy;
    @Column(columnDefinition = "datetime NULL comment '创建日期'")
    Date createDate;
    @Column(columnDefinition = "timestamp not NULL default CURRENT_TIMESTAMP comment '更新时间'")
    Date updateDate;
    //是否删除记录
    Boolean deleted = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
