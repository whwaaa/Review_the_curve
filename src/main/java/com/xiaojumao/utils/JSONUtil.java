package com.xiaojumao.utils;

import com.google.gson.Gson;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 22:38
 * @Modified By:
 */
public class JSONUtil {
    private static Gson gson = new Gson();
    public static String toJSON(Object obj){
        return gson.toJson(obj);
    }

    public static String toJSON(Object obj, String jsoncallback){
        String s = gson.toJson(obj);
        if(jsoncallback != null){
            return jsoncallback + "(" + s + ")";
        }
        return s;
    }
}
