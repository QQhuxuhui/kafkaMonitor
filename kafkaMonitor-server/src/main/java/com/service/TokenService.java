package com.service;

import com.auth0.jwt.interfaces.Claim;
import com.bean.user.UserInfo;

import java.util.Map;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/19 11:06
 * @Description:
 */
public interface TokenService {

    String sign(UserInfo userInfo);

    Map<String, Claim> verify(String token);

    String getUserName(String token);

    String expire(String token);

}
