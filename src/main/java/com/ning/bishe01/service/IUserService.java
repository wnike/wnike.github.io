package com.ning.bishe01.service;

import com.ning.bishe01.entity.User;

import java.util.List;

public interface IUserService {
    /**
     * 通过用户id查询用户信息
     * @param user
     * @return user对象
     */
    User findByUid(User user);


    List<User> findUserList(User user);
    /**
     * 增加用户信息（只允许管理员添加）
     * @param user
     * @return 插入数据数量 true表示成功；false表示未成功
     */
    boolean addUser(User user);

    /**
     * 删除用户
     * @param uid
     * @return
     */
    boolean deleteUser(String uid);

    /**
     * 修改用户
     * @param user
     * @return
     */
    boolean updateUser(User user);


}
