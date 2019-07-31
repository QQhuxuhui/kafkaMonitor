package com.common.constants;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:49
 * @Description:
 */
public enum LoginStatusEnum {
    USER_LOGIN_SUCCESS(200,"登录成功"),
    USER_LOGIN_AUTH_FAILE(201,"密码错误"),
    USER_LOGIN_NOT_VALID(202,"用户失效"),
    USER_LOGIN_LOCKED(203,"账号被锁住，请联系管理员"),
    USER_LOGIN_NO_USER(204,"用户不存在"),
    USER_LOGIN_EXPIRE(205,"账号已过期"),
    USER_NOT_LOGIN_IN_STATUS(403,"用户未登录"),
    USER_NO_RIGHT_STATUS(401,"您无权进行此操作，请联系管理员"),
    USER_VERIFY_FAILED(206,"验证失败"),
    USER_NOT_ACTIVE(207,"账号未激活");
    int code;
    String message;

    LoginStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
