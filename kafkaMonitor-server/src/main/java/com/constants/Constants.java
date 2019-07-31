package com.constants;

import java.nio.charset.Charset;

/**
 * @Auther: user
 * @Date: 2018/5/14 09:27
 * @Description:
 */
public interface Constants {
    Charset GLOBAL_CHARSET = Charset.forName("utf-8");
    //启动程序必须的xml文件路径，这个路径必须指明，否则放入storm无法扫描
//    String CORE_XML_PATH = "/*/**/*.xml";
    String CORE_XML_PATH = "/config/*.xml";
}
