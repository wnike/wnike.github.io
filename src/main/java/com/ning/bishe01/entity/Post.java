package com.ning.bishe01.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String pid;
    private String  uid;
    private String  postname;
    private String  pdescribe;
    private BigDecimal salary;
    private Integer totalNumber;
    private Integer hireNumber;
    private Integer applyPost;
    private User user;
}
