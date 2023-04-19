package com.ning.bishe01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

//@Entity(name="t_user")//映射数据库中的t_user表
@Data//@Data  //Lombok注解，生成getter setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
   // @Id //标识为主键
   // @GeneratedValue(strategy = GenerationType.IDENTITY)  //主键自增
    private String uid;//用户编号
    private String username;//用户名
    private String password;//密码
    private Integer rid;//所属的角色编号
    private String salt;//盐值
    private String grade;//班级
    private Role role;//角色
    private UData uData;//其他数据
}
