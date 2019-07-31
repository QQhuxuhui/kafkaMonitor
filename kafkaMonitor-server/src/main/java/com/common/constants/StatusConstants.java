package com.common.constants;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:44
 * @Description:
 */
public interface StatusConstants {

    String SUCCESS_STATUS = "200";
    String SUCCESS_MESSAGE = "success";


    String SERVER_EXCEPTION_STATUS = "500";
    String SERVER_EXCEPTION_MESSAGE = "服务器异常错误";


    //用户
    String USER_LOGIN_AUTH_FAILE="201";//用户名或密码错误
    Object USER_LOGIN_AUTH_FAILE_MESSAGE = "用户名或密码错误";
    String USER_LOGIN_NOT_VALID = "202";//用户失效
    Object USER_LOGIN_NOT_VALID_MESSAGE = "账号失效";
    String USER_LOGIN_LOCKED = "203";//用户被锁住
    String USER_LOGIN_LOCKED_MESSAGE = "账号被锁，无法登陆";//用户被锁住
    String  USER_LOGIN_NO_USER= "204";//用户不存在
    String  USER_LOGIN_NO_USER_MESSAGE= "用户不存在";//用户不存在
    String  USER_LOGIN_EXPIRE= "205";//用户不存在
    String  USER_LOGIN_EXPIRE_MESSAGE= "账号已过期";//用户不存在
    String  USER_LOGIN_SUCCESS= "200";//登陆成功
    String  USER_LOGIN_SUCCESS_MESSAGE= "登陆成功";//登陆成功
    String  USER_VERIFY_FAILED= "206";//验证失败
    String  USER_VERIFY_FAILED_MESSAGE= "验证失败";//验证失败

    String USER_LOGINOUT_SUCCESS_STATUS = "200";
    String USER_LOGINOUT_SUCCESS_MESSAGE = "登陆成功";

    String USER_NOT_LOGIN_IN_STATUS="403";
    String USER_NOT_LOGIN_IN_MESSAGE="用户未登录";

    //权限
    String USER_NO_RIGHT_STATUS="401";
    String USER_NO_RIGHT_MESSAGE="您无权进行此操作！！";


}


