package com.ning.bishe01.service;

import com.ning.bishe01.entity.Order;

import java.util.List;

public interface IOrderService {
    /**
     * 通过订单id查询订单信息
     * @param order
     * @return order对象
     */
    Order findByOid(Order order);
    Order findStuByStuuid(Order order);
    Order findEntByEntuid(Order order);
    Order findOrderByPid(Order order);
    Order findByPidAndStuuid(Order order);

    List<Order> findOrderList(Order order);


    List<Order> findOrderListByEntuid(Order order);
    List<Order> findOrderListByEntuidWork(Order order);
    /**
     * 增加订单信息
     * @param order
     * @return 插入数据数量 true表示成功；false表示未成功
     */
    boolean addOrder(Order order);

    /**
     * 删除订单
     * @param oid
     * @return
     */
    boolean deleteOrder(String oid);

    /**
     * 修改订单
     * @param order
     * @return
     */
    String updateOrder(Order order);
boolean updateOrderAdmin(Order order,Integer num);


}
