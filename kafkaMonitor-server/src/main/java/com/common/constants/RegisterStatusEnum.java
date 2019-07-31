package com.common.constants;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/24 16:55
 * @Description:
 */
public enum RegisterStatusEnum {
    REGISTER_SUCCESS(200,"注册成功"),
    NAME_EXIST(201,"昵称已存在"),
    USERNAME_EXIST(202,"账号已存在"),
    EMAIL_EXIST(203,"邮箱已被注册");
    int code;
    String message;

    RegisterStatusEnum(int code, String message) {
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
