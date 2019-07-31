package com.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/2 11:40
 * @Description: 需要监控的记录IDS和email地址关联表
 */
@Entity
public class IdAndEmail {
    @Id
    @Column(columnDefinition = "varchar(32) comment '主键，UUID'")
    String id;
    //关联表的Id
    @Column(columnDefinition = "varchar(32) not null comment '关联表的Id'")
    String ids;
    @Column(columnDefinition = "varchar(20) not null comment '需要通知的email'")
    String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
