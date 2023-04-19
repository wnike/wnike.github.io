package com.ning.bishe01.service.impl;


import com.ning.bishe01.entity.Role;
import com.ning.bishe01.mapper.RoleMapper;
import com.ning.bishe01.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    //查询——用户——通过rid
    @Override
    public Role findByRid(Role role) {
        Role result = roleMapper.selectByRid(role);
        //未查询到返回空
        if (result == null) {
//            throw new RolenameNofindException("用户名或者密码错误");
            return null;
        }
        //密码不对（并且传来的Role的密码不为空，因为除了登陆密码传来的都为null）返回空

        return result;
    }

    @Override
    public List<Role> findRoleList() {
        return roleMapper.selectRoleList();
    }

    //添加——用户
    @Override
    public boolean addRole(Role role) {
        Role result = roleMapper.selectByRid(role);
        //如果结果为空（即未注册）可以插入
        if (result == null) {
            //随机生成盐值
            String salt = UUID.randomUUID().toString().toUpperCase();
            //初始密码全部设置成123456

            Integer row = roleMapper.insertRole(role);
            // 判断受影响的行数是否不为1
            if (row != 1) {
                // 是：插入数据时出现某种错误，则抛出InsertException异常
//                throw new InsertException("插入数据异常");
                return false;
            }
        } else {
//            throw new InsertException("id已存在");
            return false;
        }
        return true;
    }

    //删除用户
    @Override
    public boolean deleteRole(String rid) {
        Integer row = roleMapper.deleteRole(rid);
        if (row != 1) {
            return false;
        }
        return true;
    }

    //修改用户信息
    @Override
    public boolean updateRole(Role role) {
        //随机生成盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        //初始密码全部设置成123456
        role.setDescription("123456");
        //通过md5加密

        Integer row = roleMapper.updateRole(role);
        if(row!=1){
            return false;
        }
        return true;
    }


    /**
     * 加密密码——MD5执行密码加密
     *
     * @param password 原始密码
     * @param salt     盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password +
                    salt).getBytes()).toUpperCase();
        }
        return password;
    }

}
