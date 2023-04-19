package com.ning.bishe01.service;

import com.ning.bishe01.entity.Role;

import java.util.List;

public interface IRoleService {
    /**
     * 通过用户id查询用户信息
     * @param role
     * @return role对象
     */
    Role findByRid(Role role);


    List<Role> findRoleList();
    /**
     * 增加用户信息（只允许管理员添加）
     * @param role
     * @return 插入数据数量 true表示成功；false表示未成功
     */
    boolean addRole(Role role);

    /**
     * 删除用户
     * @param rid
     * @return
     */
    boolean deleteRole(String rid);

    /**
     * 修改用户
     * @param role
     * @return
     */
    boolean updateRole(Role role);


}
