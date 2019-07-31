package com.bean.shell;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/29 10:46
 * @Description:
 */
public class ShellResponseBean {

    //200成功，201失败,202异常
    int code = ShellResponseCodeEnum.SUCCESS.getCode();
    String msg = "success";
    //返回的结果
    List<String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
