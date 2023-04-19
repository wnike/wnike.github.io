package com.ning.bishe01.service.impl;


import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.mapper.UDataMapper;
import com.ning.bishe01.mapper.UserMapper;
import com.ning.bishe01.service.IUserService;
import com.ning.bishe01.service.ex.InsertException;
import com.ning.bishe01.service.ex.UsernameNofindException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UDataMapper uDataMapper;

    //查询——用户——通过uid
    @Override
    public User findByUid(User user) {
        User result = userMapper.selectByUid(user);
        //未查询到返回空
        if (result == null) {
            return null;
        }

//        //密码不对（并且传来的user的密码不为空，因为除了登陆密码传来的都为null）返回空
//        if ((!result.getPassword().equals(user.getPassword())) && (user.getPassword() != null)) {
//            return result;
//        }
        return result;
    }

    //查询——用户列表
    @Override
    public List<User> findUserList(User user) {
        return userMapper.selectUserList(user);
    }

    //添加——用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(User user) {
        User result = userMapper.selectByUid(user);
        //如果结果为空（即未注册）可以插入
        try {
            if (result == null) {
                //随机生成盐值
                String salt = UUID.randomUUID().toString().toUpperCase();
                //初始密码全部设置成123456
                user.setPassword("123456");
                //通过md5加密
                String md5Password = getMd5Password(user.getPassword(), salt);
                //设置MD5密码
                user.setPassword(md5Password);
                // 补全数据：盐值
                user.setSalt(salt);
                UData uData = new UData();
                uData.setUid(user.getUid());
                uData.setUsersex("未填写");
                uData.setUsersalary(new BigDecimal("00.00"));
                Integer row = userMapper.insertUser(user);
                // 判断受影响的行数是否不为1
                if (row != 1) {
                    throw new Exception("错误");
                } else {
                    Integer row_data = uDataMapper.insertUData(uData);
                    if (row_data != 1) {
                        throw new Exception("错误");
                    }
                }
            } else {
                throw new Exception("错误");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return false;
        }
        return true;
    }

    //删除用户,事务回滚
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String uid) {
        try {
            Integer row = userMapper.deleteUser(uid);
            if (row != 1) {
                throw new Exception("错误");
            } else {
                Integer row_data = uDataMapper.deleteUData(uid);
                if (row_data != 1) {
                    throw new Exception("错误");
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return false;
        }
        return true;
    }

    //修改用户信息
    @Override
    public boolean updateUser(User user) {
        //随机生成盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        //通过md5加密
        String md5Password = getMd5Password(user.getPassword(), salt);
        //设置MD5密码
        user.setPassword(md5Password);
        // 补全数据：盐值
        user.setSalt(salt);
        Integer row = userMapper.updateUser(user);
        if (row != 1) {
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
