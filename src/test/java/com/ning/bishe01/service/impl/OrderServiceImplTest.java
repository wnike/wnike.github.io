package com.ning.bishe01.service.impl;

import com.ning.bishe01.entity.Order;
import com.ning.bishe01.mapper.OrderMapper;
import com.ning.bishe01.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private IOrderService orderService;

    @Test
    public void addOrderTest() {
        Order order=new Order();
        order.setStuuid("1");
        order.setEntuid("2");
        order.setPid("3");
        boolean result = orderService.addOrder(order);
        System.err.println("结果为："+result);
    }

    @Test
    public void findByOidTest(){
        String s="1680607284822-1-2-3";
        Order order=new Order();
        order.setOid(s);
        Order result=orderService.findByOid(order);
        System.err.println("结果为："+result);
    }
    @Test
    public void findOrderListTest(){
        Order order=new Order();
        List<Order> result=orderService.findOrderList(order);
        for(Order oo:result){
            System.err.println("结果为："+oo);
        }

    }


}