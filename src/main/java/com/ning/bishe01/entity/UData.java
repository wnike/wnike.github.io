package com.ning.bishe01.entity;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UData {
    private String uid;
    private String usersex;//性别
    private String userdescribe;//自我描述
    private BigDecimal usersalary;//用户薪资
    private Integer userpriority;//贫困优先级
}
