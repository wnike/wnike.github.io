package com.ning.bishe01.service;

import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;

import java.util.List;

public interface IUDataService {
    /**
     * 通过用户id查询用户信息
     * @param uData
     * @return uData对象
     */
    UData findUDataByUid(UData uData);


    List<UData> findUDataList(UData uData);
    /**
     * 增加用户信息（只允许管理员添加）
     * @param uData
     * @return 插入数据数量 true表示成功；false表示未成功
     */
    boolean addUData(UData uData);

    /**
     * 删除用户
     * @param uid
     * @return
     */
    boolean deleteUData(String uid);

    /**
     * 修改用户
     * @param uData
     * @return
     */
    boolean updateUData(UData uData , User user);

    boolean addUDataMoney(UData uData);

}
