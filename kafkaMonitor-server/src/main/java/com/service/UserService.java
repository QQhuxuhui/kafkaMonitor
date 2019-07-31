package com.service;

import com.bean.ResponseBean;
import com.bean.user.UserInfo;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:39
 * @Description:
 */
public interface UserService {

    /**
     * 用户登录
     * @param userInfo
     * @return
     */
    UserInfo login(UserInfo userInfo);


    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    UserInfo getUserInfoByUserName(String username);

    /**
     * 用户注册
     * @param userInfo
     * @return
     */
    ResponseBean register(UserInfo userInfo);

    /**
     * 账号激活
     * @param userInfo
     * @return
     */
    ResponseBean active(UserInfo userInfo);


}
