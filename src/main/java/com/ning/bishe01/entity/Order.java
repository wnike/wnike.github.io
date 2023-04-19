package com.ning.bishe01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String oid;//申请application岗位order单号id，自动生成订单格式包含所有信息用下划线_分割，用时间戳防止重复
    private String  stuuid;//student学生id
    private String  entuid;//enterprise单位id
    private String  pid;//岗位id
    private Integer finishwork;//工作是否完成（由用工单位判断），0未完成（默认0未完成），1完成
    private Integer finishsalary;//薪资是否结算，0未结算（默认0未结算），1结算
    private Integer finishapplywork;//该申请表是否已经通过审核，默认为0，1通过用工单位审核，2通过管理员审核可以工作，5未通过岗位申请审核
    private Date applytime;//申请成功时间
    private Date worktime;//工作完成时间
    private Integer state;//状态:默认为0，申请中为1，申请完成就是工作中为2，工作完成未结算为3，结算工资为4，没通过审核为5，可以为finishapplywork+finishwork+finishsalary的和
}

