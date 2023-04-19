package com.ning.bishe01.service.impl;


import com.ning.bishe01.entity.Order;
import com.ning.bishe01.entity.Post;
import com.ning.bishe01.entity.UData;
import com.ning.bishe01.mapper.OrderMapper;
import com.ning.bishe01.mapper.PostMapper;
import com.ning.bishe01.mapper.UDataMapper;
import com.ning.bishe01.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;


@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UDataMapper uDataMapper;

    //查询——订单——通过oid
    @Override
    public Order findByOid(Order order) {
        Order result = orderMapper.selectByOid(order);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Order findStuByStuuid(Order order) {
        Order result = orderMapper.selectStuByStuuid(order);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Order findEntByEntuid(Order order) {
        Order result = orderMapper.selectEntByEntuid(order);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Order findOrderByPid(Order order) {
        Order result = orderMapper.selectOrderByPid(order);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Order findByPidAndStuuid(Order order) {
        Order result = orderMapper.selectByPidAndStuuid(order);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    //查询——订单列表
    @Override
    public List<Order> findOrderList(Order order) {
        return orderMapper.selectOrderList(order);
    }

    //用工单位——查询——订单列表
    @Override
    public List<Order> findOrderListByEntuid(Order order) {
        return orderMapper.selectOrderListByEntuid(order);
    }

    //用工单位——查询——订单列表
    @Override
    public List<Order> findOrderListByEntuidWork(Order order) {
        return orderMapper.selectOrderListByEntuidWork(order);
    }

    //添加——订单
    @Override
    public boolean addOrder(Order order) {
        Post post = postMapper.selectUidByPid(order.getPid());
        Date sqlDate = new java.sql.Date(new Date().getTime());
        //设置用工单位id
        order.setEntuid(post.getUid());
        String time = String.valueOf(sqlDate.getTime());
        //添加申请时间
        order.setApplytime(sqlDate);
        order.setWorktime(null);
        //获取前端传来的数据
        String stuuid = order.getStuuid();
        String entuid = order.getEntuid();
        String pid = order.getPid();
        //设置组合oid格式
        String oid = time + "-" + stuuid + "-" + entuid + "-" + pid;
        //添加oid
        order.setOid(oid);
        order.setFinishwork(0);
        order.setFinishsalary(0);
        order.setFinishapplywork(0);
        Integer row = orderMapper.insertOrder(order);
        // 判断受影响的行数是否不为1
        if (row != 1) {
            // 是：插入数据时出现某种错误
            return false;
        }
        return true;
    }

    //删除订单
    @Override
    public boolean deleteOrder(String oid) {
        Integer row = orderMapper.deleteOrder(oid);
        if (row != 1) {
            return false;
        }
        return true;
    }

    //修改订单信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateOrder(Order order) {
        //finishwork为1,表示工作完成阶段
        try {
            if (order.getFinishwork() == 1) {
                //减少岗位已录取数量
                Post post = postMapper.selectUidByPid(order.getPid());
                if (post == null) {
                    throw new Exception("错误");
                }
                post.setHireNumber(post.getHireNumber() - 1);
                Integer result = postMapper.updatePostAdmin(post);
                if (result != 1) {
                    throw new Exception("错误");
                }
                if (order.getFinishsalary() == 1) {
                    //减少自己余额,增加别人余额
                    //先查询自己用户余额
                    UData muData = new UData();
                    muData.setUid(order.getEntuid());
                    UData MyUData = uDataMapper.selectUDataByUid(muData);
                    BigDecimal mysalary = MyUData.getUsersalary();
//                System.err.println("用工单位="+mysalary);
                    //获取岗位薪水
                    Post p = new Post();
                    p.setPid(order.getPid());
                    Post pPrice = postMapper.selectPostByPid(p);
                    BigDecimal salary = pPrice.getSalary();
//                System.err.println("每日工资="+salary);
                    //Post里的工资是每日的,所以获取申请成功时间和完成时间,取整
                    long time_long = getDatePoor(order.getWorktime(), order.getApplytime());//获取时间差
                    int timeSalary = (int) time_long;
                    BigDecimal timeSalary_bigDecimal = new BigDecimal(Integer.toString(timeSalary));//integer装换为bigDecimal
//                System.err.println("天数="+timeSalary_bigDecimal);
                    salary = salary.multiply(timeSalary_bigDecimal);//得到学生应该获得的薪资
                    //判断自己余额是否够大于岗位薪水,不能支付时报错
                    if (salary.compareTo(mysalary) == 1) {//前者大于后者
                        throw new Exception("余额不足");
                    }
                    //扣除自己余额
//                System.err.println("学生应该获得工资="+salary);
                    mysalary = mysalary.subtract(salary);
                    MyUData.setUsersalary(mysalary);
//                System.err.println("用工单位结算后工资="+mysalary);
                    //根据别人id增加相同数量
                    UData suData = new UData();
                    suData.setUid(order.getStuuid());
                    UData StuUData = uDataMapper.selectUDataByUid(suData);
                    BigDecimal stusalary = StuUData.getUsersalary();
//                System.err.println("学生工资="+stusalary);
                    stusalary = stusalary.add(salary);
                    StuUData.setUsersalary(stusalary);
//                System.err.println("学生结算后工资="+stusalary);
                    //更新数据
                    Integer l = uDataMapper.updateUData(MyUData);
                    Integer l2 = uDataMapper.updateUData(StuUData);
                    if (l != 1 || l2 != 1) {
                        throw new Exception("错误");
                    }
                } else {
                    //该处为完成工作时,此时finishwork=1且finishsalay=0
                    Date sqlDate = new java.sql.Date(new Date().getTime());
                    order.setWorktime(sqlDate);
                }
            }
            Integer row = orderMapper.updateOrder(order);
            if (row != 1) {
                throw new Exception("错误");
            }
        } catch (Exception e) {
//    e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return e.getMessage();
        }
        return "1";
    }

    //修改订单信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderAdmin(Order order, Integer finishapplywork) {
        try {
            //通过时同时修改post中录取数量,+1;
            if (finishapplywork == 2) {
                Post post = postMapper.selectUidByPid(order.getPid());
                if (post == null) {
                    throw new Exception("错误");
                }
                //如果已经满员,设置所有该pid的订单(申请中)的finishapplywork为10,即不通过;
                if (post.getHireNumber() >= post.getTotalNumber()) {
                    List<Order> orderList = orderMapper.selectOrderListByPid(order.getPid());
                    for (Order oo : orderList) {
                        //申请中的更改
                        if (oo.getFinishapplywork() <= 1) {
                            oo.setFinishapplywork(10);
                            Integer row = orderMapper.updateOrder(oo);
                            if (row != 1) {
                                throw new Exception("错误");
                            }
                        }
                    }
                    return true;
                }
                //增加已录取数量
                post.setHireNumber(post.getHireNumber() + 1);
                Integer result = postMapper.updatePostAdmin(post);
                if (result != 1) {
                    throw new Exception("错误");
                }
            }
            Date sqlDate = new java.sql.Date(new Date().getTime());
            //设置申请完成时间
            order.setApplytime(sqlDate);
            Integer row = orderMapper.updateOrder(order);
            if (row != 1) {
                throw new Exception("错误");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//try catch捕获异常会让注解失效
            return false;
        }
        return true;
    }


    public long getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
//        return day + "天" + hour + "小时" + min + "分钟";
        return day;
    }

}
