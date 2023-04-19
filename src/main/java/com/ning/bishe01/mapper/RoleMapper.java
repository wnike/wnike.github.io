package com.ning.bishe01.mapper;

import com.ning.bishe01.entity.Role;

import java.util.List;

//@Mapper
//@Repository
public interface RoleMapper {
    /**
     * 通过用户id查询用户信息
     * @param role
     * @return  user对象
     */
    Role selectByRid(Role role);

    /**
     * 查询user列表
     * @return
     */

    List<Role> selectRoleList();

    /**
     * 增加用户信息（只允许管理员添加）
     * @param role
     * @return   返回添加数量
     */
    Integer insertRole(Role role);

    /**
     * 删除用户信息
     * @param rid
     * @return
     */
    Integer deleteRole(String rid);


    /**
     * 修改用户信息
     * @param role
     * @return
     */
    Integer updateRole(Role role);


}
