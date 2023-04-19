package com.ning.bishe01.mapper;

import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;

import java.util.List;

//@Mapper
//@Repository
public interface UDataMapper {
    /**
     * 通过用户id查询用户信息
     * @param uData
     * @return  uData对象
     */
    UData selectUDataByUid(UData uData);

    /**
     * 查询uData列表
     * @param uData
     * @return
     */

    List<UData> selectUDataList(UData uData);

    /**
     * 增加用户信息（只允许管理员添加）
     * @param uData
     * @return   返回添加数量
     */
    Integer insertUData(UData uData);

    /**
     * 删除用户信息
     * @param uid
     * @return
     */
    Integer deleteUData(String uid);


    /**
     * 修改用户信息
     * @param uData
     * @return
     */
    Integer updateUData(UData uData);

Integer updateUDataMoney(UData uData);

}
