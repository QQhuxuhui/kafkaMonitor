package com.bean;

import javax.persistence.*;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/2 11:26
 * @Description: 保存系统的邮件信息
 */
@Entity
public class EmailAddress {
    @Id
    @Column(columnDefinition = "varchar(32) comment 'key UUID'")
    String id;
    @Column(columnDefinition = "varchar(20) not null comment ''")
    String email;
    @Column(columnDefinition = "varchar(20) default null comment ''")
    String name;
    @Column(columnDefinition = "varchar(10) default null comment ''")
    String password;
    @Column(columnDefinition = "varchar(10) default null comment 'tag'")
    String tag;
    @Column(columnDefinition = "boolean not null default false comment 'is delete'")
    Boolean deleted = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
