package com.xiaojumao.utils;

import java.util.Random;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 16:17
 * @Modified By:
 */
public class RandomUtil {
    private static Random random = new Random();

    /**
     * 随机生成6位数
     * @return 6位随机取件码
     */
    public static String getCode(){
        int i = random.nextInt(900000)+100000;
        return String.valueOf(i);
    }

    /**
     * 随机生成32位字符
     * @return 32位字符
     */
    public static String getToken(){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<32; i++){
            sb.append((char)bytes[random.nextInt(str.length())]);
        }
        return sb.toString();
    }
}
