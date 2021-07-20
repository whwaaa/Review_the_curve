package com.xiaojumao.dao.imp;

import com.xiaojumao.bean.User;
import com.xiaojumao.dao.UserDao;
import com.xiaojumao.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImp extends DBUtils implements UserDao {
    // 添加新用户
    private static final String ADD_USER_SQL = "INSERT INTO USER(EMAIL,PASSWORD,TOKEN) VALUES(?,?,?)";
    // 通过邮箱查询用户信息
    private static final String GET_USER_BY_EMAIL_SQL = "SELECT * FROM USER WHERE EMAIL=?";
    // 通过uid查询用户信息
    private static final String GET_USER_BY_UID_SQL = "SELECT * FROM USER WHERE UID=?";
    // 通过token查询用户信息
    private static final String GET_USER_BY_TOKEN_SQL = "SELECT * FROM USER WHERE TOKEN=?";

    /**
     * 添加新用户
     * @param user:新用户信息
     * @return
     */
    @Override
    public Integer addUser(User user) {
        List param = new ArrayList<>();
        param.add(user.getEmail());
        param.add(user.getPassword());
        param.add(user.getToken());
        try {
            return update(ADD_USER_SQL, param);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 通过邮箱查询用户信息
     * @param email:查询的邮箱
     * @return
     */
    @Override
    public User getUserByEmail(String email) {
        List param = new ArrayList();
        param.add(email);
        try {
            ResultSet resultSet = query(GET_USER_BY_EMAIL_SQL, param);
            if(resultSet != null){
                while(resultSet.next()){
                    int uid = resultSet.getInt("uid");
                    String password = resultSet.getString("password");
                    String token = resultSet.getString("token");
                    return new User(uid, email, password, token);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 通过uid查询用户信息
     * @param uid:需要查询的用户uid
     * @return
     */
    @Override
    public User getUserByUid(Integer uid) {
        List param = new ArrayList();
        param.add(uid);
        try {
            ResultSet resultSet = query(GET_USER_BY_UID_SQL, param);
            if(resultSet != null){
                while(resultSet.next()){
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String token = resultSet.getString("token");
                    return new User(uid, email, password, token);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 通过token查询用户信息
     * @param token:需要查询的用户token
     * @return
     */
    @Override
    public User getUserByToken(String token) {
        List param = new ArrayList();
        param.add(token);
        try {
            ResultSet resultSet = query(GET_USER_BY_TOKEN_SQL, param);
            if(resultSet != null){
                while(resultSet.next()){
                    Integer uid = resultSet.getInt("uid");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    return new User(uid, email, password, token);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

}
