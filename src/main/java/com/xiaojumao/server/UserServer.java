package com.xiaojumao.server;

import com.xiaojumao.bean.User;
import com.xiaojumao.dao.imp.UserDaoImp;

public class UserServer {
    private static UserDaoImp dao = new UserDaoImp();

    public Integer addUser(User user) {
        return dao.addUser(user);
    }

    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    /**
     * 通过uid查询用户信息
     * @param uid:需要查询的用户uid
     * @return
     */
    public User getUserByUid(Integer uid){
        return dao.getUserByUid(uid);
    }

    /**
     * 通过token查询用户信息
     * @param token:需要查询的用户token
     * @return
     */
    public User getUserByToken(String token) {
        return dao.getUserByToken(token);
    }
}
