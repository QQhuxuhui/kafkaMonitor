package com.bean.shell;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/29 10:48
 * @Description:
 */
public enum ShellResponseCodeEnum {
    SUCCESS(200,"成功"),
    FAILE(201,"错误指令"),
    EXCEPTION(203, "发生异常");
    private int code;
    private String desc;

    ShellResponseCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
