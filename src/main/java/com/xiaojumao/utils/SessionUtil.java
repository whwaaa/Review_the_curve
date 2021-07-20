package com.xiaojumao.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class SessionUtil {

    private static Map<String, String> emailVerCode = new HashMap<>();

    public static void setEmailVerCode(String email, String verCode){
        emailVerCode.put(verCode, email);
//        // TODO: 新增application内置对象保存验证码
//        Map<String, String> map = new HashMap<>();
//        map.put(verCode, email);
//        req.getSession().getServletContext().setAttribute(varToken,map);
    }

    public static boolean isEmailVerCode(String email, String verCode){
        if(email.equals(emailVerCode.get(verCode)))
            return true;
//        // TODO: 新增application内置对象保存验证码
//        Map<String, String> map = (Map<String, String>)req.getSession().getServletContext().getAttribute(varToken);
//        if(map != null){
//            if(email.equals(map.get(verCode)))
//                return true;
//        }
        return false;
    }

    public static Integer getUid(HttpServletRequest res, HttpServletResponse resp){
        Object uidO = res.getSession().getAttribute("uid");
        if (uidO != null) {
            return Integer.parseInt((String)uidO);
        }
        return null;
    }

    public static void setUid(HttpServletRequest res, Integer uid){
        res.getSession().setAttribute("uid",uid.toString());
    }
}
