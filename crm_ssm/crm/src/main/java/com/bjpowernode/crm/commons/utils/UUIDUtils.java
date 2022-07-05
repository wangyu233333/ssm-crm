package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

//  获取UUID的值
public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
