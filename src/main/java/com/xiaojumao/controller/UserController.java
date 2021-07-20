package com.xiaojumao.controller;

import com.xiaojumao.bean.Message;
import com.xiaojumao.bean.User;
import com.xiaojumao.mvc.ResponseBody;
import com.xiaojumao.server.UserServer;
import com.xiaojumao.utils.EmailSend;
import com.xiaojumao.utils.JSONUtil;
import com.xiaojumao.utils.RandomUtil;
import com.xiaojumao.utils.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController {

    private static UserServer server = new UserServer();

    @ResponseBody("/user/checkToken.do")
    public String checkToken(HttpServletRequest req, HttpServletResponse resp) {
        String jsoncallback = req.getParameter("jsoncallback");
        // 获取token参数
        String token = req.getParameter("token");
        // 查询user
        User user = null;
        if (token != null) {
            user = server.getUserByToken(token);
        }
        if (user != null) {
            // token查询到用户,直链登录成功
            SessionUtil.setUid(req, user.getUid());
            return JSONUtil.toJSON(new Message(0, "直链登录成功", user), jsoncallback);
        }
        return JSONUtil.toJSON(new Message(-1, "", null), jsoncallback);
    }

    @ResponseBody("/user/getVerCode.do")
    public String getVerCode(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        // 获取邮箱
        String email = req.getParameter("email");
        // 存入验证码和邮箱
        String code = RandomUtil.getCode();
        SessionUtil.setEmailVerCode(email, code);
        // 发送邮件
        EmailSend.sendLoginVer(email, code);
        return JSONUtil.toJSON(new Message(0, "已发送", null),jsoncallback);
    }

    @ResponseBody("/user/login.do")
    public String login(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        // 获取邮箱、验证码
        String email = req.getParameter("email");
        String verCode = req.getParameter("verCode");
        // 验证vercode和email
        boolean isEmailVerCode = SessionUtil.isEmailVerCode(email, verCode);
        if(isEmailVerCode){
            User user = server.getUserByEmail(email);
            if(user != null){
                // 注册过,session存入登录信息
                SessionUtil.setUid(req, user.getUid());
                return JSONUtil.toJSON(new Message(0, "登录成功", user),jsoncallback);
            }else{
                // 未注册过,注册
                user = new User();
                user.setEmail(email);
                user.setToken(RandomUtil.getToken());
                Integer integer = server.addUser(user);
                user = server.getUserByEmail(email);
                // 存入登录信息
                SessionUtil.setUid(req, user.getUid());
                return JSONUtil.toJSON(new Message(0, "注册成功", user),jsoncallback);
            }
        }

        return JSONUtil.toJSON(new Message(-1, "邮箱或验证码不匹配", null),jsoncallback);
    }
}
