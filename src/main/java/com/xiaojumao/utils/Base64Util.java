package com.xiaojumao.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    public static String stringToBase64(String str){
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String base64ToString(String base64Str){
        if (base64Str != null) {
            try {
                String ss = base64Str;
                byte[] decode = Base64.getDecoder().decode(base64Str);
                return new String(decode, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
