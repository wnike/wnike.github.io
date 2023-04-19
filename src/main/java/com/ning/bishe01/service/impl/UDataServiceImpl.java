package com.ning.bishe01.service.impl;


import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.mapper.UDataMapper;
import com.ning.bishe01.mapper.UDataMapper;
import com.ning.bishe01.mapper.UserMapper;
import com.ning.bishe01.service.IUDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class UDataServiceImpl implements IUDataService {
    @Autowired
    private UDataMapper uDataMapper;
    @Autowired
    private UserMapper userMapper;

    //查询——用户——通过uid
    @Override
    public UData findUDataByUid(UData uData) {
        UData result = uDataMapper.selectUDataByUid(uData);
        //未查询到返回空
        if (result == null) {
            return null;
        }

        //密码不对（并且传来的uData的密码不为空，因为除了登陆密码传来的都为null）返回空
//       if((!result.getPassword().equals(uData.getPassword()))&&(uData.getPassword()!=null)){
//           return null;
//       }
        return result;
    }

    //查询——用户列表
    @Override
    public List<UData> findUDataList(UData uData) {
        return uDataMapper.selectUDataList(uData);
    }

    //添加——用户
    @Override
    public boolean addUData(UData uData) {
        UData result = uDataMapper.selectUDataByUid(uData);
        //如果结果为空（即未注册）可以插入
        if (result == null) {
            uData.setUsersex("未填写");
            uData.setUsersalary(new BigDecimal("00.00"));
            Integer row = uDataMapper.insertUData(uData);
            // 判断受影响的行数是否不为1
            if (row != 1) {
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
    public boolean deleteUData(String uid) {
        Integer row = uDataMapper.deleteUData(uid);
        if (row != 1) {
            return false;
        }
        return true;
    }

    //修改用户信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUData(UData uData, User user) {
        try {
            Integer row = uDataMapper.updateUData(uData);
            Integer row2 = userMapper.updateUser(user);
            if (row != 1||row2 != 1) {
                throw new Exception("错误");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return false;
        }
        return true;
    }

    //充值余额
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUDataMoney(UData uData) {
        try {
            UData olduData = uDataMapper.selectUDataByUid(uData);
            BigDecimal newMoney = olduData.getUsersalary().add(uData.getUsersalary());
            uData.setUsersalary(newMoney);
            Integer result = uDataMapper.updateUDataMoney(uData);
            if (result != 1) {
                throw new Exception("错误");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return false;
        }
        return true;
    }

}
