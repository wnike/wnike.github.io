package com.ning.bishe01.mapper;

import com.ning.bishe01.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
//@Repository
public interface UserMapper {
    /**
     * 通过用户id查询用户信息
     * @param user
     * @return  user对象
     */
    User selectByUid(User user);

    /**
     * 查询user列表
     * @param user
     * @return
     */

    List<User> selectUserList(User user);

    /**
     * 增加用户信息（只允许管理员添加）
     * @param user
     * @return   返回添加数量
     */
    Integer insertUser(User user);

    /**
     * 删除用户信息
     * @param uid
     * @return
     */
    Integer deleteUser(String uid);


    /**
     * 修改用户信息
     * @param user
     * @return
     */
    Integer updateUser(User user);


}
