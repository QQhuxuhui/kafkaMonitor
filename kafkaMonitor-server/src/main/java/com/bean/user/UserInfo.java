package com.bean.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**  
 * 用户信息.  
 * @author Administrator  
 *  
 */  
@Entity
public class UserInfo implements Serializable {
  
    /**  
     *   
     */  
    private static final long serialVersionUID = 1L;  
  
    @Id  
    @GeneratedValue  
    private long uid;// 用户id  
  
    @Column(unique = true)  
    private String username;// 帐号  
  
    private String name;// 名称（昵称或者真实姓名，不同系统不同定义）  
  
    private String password; // 密码;
    private String email;//注册邮箱
    private String salt;// 加密密码的盐

    private String token;
    @Column(columnDefinition = "int default 200 comment '200:正常;202:用户失效;203:账号被锁;205:账号过期;207:账号未激活'")
    private int state;//详见LoginStatusEnum

    @Column(columnDefinition = "timestamp not NULL default CURRENT_TIMESTAMP comment '创建时间'")
    private Date createDate;
  
    @ManyToMany(fetch = FetchType.EAGER) // 立即从数据库中进行加载数据  
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns = {  
            @JoinColumn(name = "roleId") })  
    private List<SysRole> roleList;// 一个用户具有多个角色  
  
    public long getUid() {  
        return uid;  
    }  
  
    public void setUid(long uid) {  
        this.uid = uid;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
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
  
    public String getSalt() {  
        return salt;  
    }  
  
    public void setSalt(String salt) {  
        this.salt = salt;  
    }  
  
    public int getState() {
        return state;  
    }  
  
    public void setState(int state) {
        this.state = state;  
    }  
  
    public List<SysRole> getRoleList() {  
        return roleList;  
    }  
  
    public void setRoleList(List<SysRole> roleList) {  
        this.roleList = roleList;  
    }  
  
    /**  
     * 密码盐.  
     *   
     * @return  
     */  
    public String getCredentialsSalt() {  
        return this.username + this.salt;  
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                ", token='" + token + '\'' +
                ", state=" + state +
                ", createDate=" + createDate +
                ", roleList=" + roleList +
                '}';
    }
}
