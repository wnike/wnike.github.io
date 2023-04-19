package com.ning.bishe01.mapper;


import com.ning.bishe01.entity.Order;

import java.util.List;

public interface OrderMapper {


        /**
         * 通过订单id查询订单信息
         * @param order
         * @return  order对象
         */
        Order selectByOid(Order order);

        Order selectStuByStuuid(Order order);

        Order selectEntByEntuid(Order order);

        Order selectOrderByPid(Order order);

        Order selectByPidAndStuuid(Order order);

        /**
         * 查询order列表
         * @param order
         * @return
         */

        List<Order> selectOrderList(Order order);
List<Order> selectOrderListByEntuid(Order order);
        List<Order> selectOrderListByEntuidWork(Order order);
List<Order>selectOrderListByPid(String pid);
        /**
         * 增加订单信息
         * @param order
         * @return   返回添加数量
         */
        Integer insertOrder(Order order);

        /**
         * 删除订单信息
         * @param oid
         * @return
         */
        Integer deleteOrder(String oid);


        /**
         * 修改订单信息
         * @param order
         * @return
         */
        Integer updateOrder(Order order);


    }
