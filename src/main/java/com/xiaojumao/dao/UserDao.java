package com.xiaojumao.dao;

import com.xiaojumao.bean.User;

public interface UserDao {

    /**
     * 添加新用户
     * @param user:新用户信息
     * @return
     */
    public Integer addUser(User user);

    /**
     * 通过邮箱查询用户信息
     * @param email:查询的邮箱
     * @return
     */
    public User getUserByEmail(String email);

    /**
     * 通过uid查询用户信息
     * @param uid:需要查询的用户uid
     * @return
     */
    public User getUserByUid(Integer uid);

    /**
     * 通过token查询用户信息
     * @param token:需要查询的用户token
     * @return
     */
    public User getUserByToken(String token);
}
