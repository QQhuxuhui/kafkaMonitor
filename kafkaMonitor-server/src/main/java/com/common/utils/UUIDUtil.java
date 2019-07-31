package com.common.utils;

import java.util.UUID;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 11:07
 * @Description:
 */
public class UUIDUtil {
    public static String getUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

}
