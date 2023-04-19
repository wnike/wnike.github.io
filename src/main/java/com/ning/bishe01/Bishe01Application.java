package com.ning.bishe01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.ning.bishe01.mapper")
@EnableTransactionManagement
public class Bishe01Application {

    public static void main(String[] args) {
        SpringApplication.run(Bishe01Application.class, args);
    }

}
