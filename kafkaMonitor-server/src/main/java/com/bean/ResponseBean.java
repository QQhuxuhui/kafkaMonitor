package com.bean;


import java.io.Serializable;

/**
 * @Auther: user
 * @Date: 2018/7/4 13:42
 * @Description:
 */
public class ResponseBean implements Serializable{
    String msg = "success";
    int code = 200;
    Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
