package com.ning.bishe01.controller;


import com.github.pagehelper.PageInfo;
import com.ning.bishe01.entity.Order;
import com.ning.bishe01.entity.Post;
import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.service.IOrderService;
import com.ning.bishe01.service.IPostService;
import com.ning.bishe01.service.IUDataService;
import com.ning.bishe01.service.IUserService;
import javafx.geometry.Pos;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class OrderController extends BaseController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;

    @Autowired
    private IUDataService uDataService;

    /**
     * 添加订单redirect:
     *
     * @param order
     * @return
     */
    @RequestMapping("/add_order")
    public ModelAndView addOrder(Post post, Order order) {
        ModelAndView mv = new ModelAndView("forward:/post_list");
        Order result = orderService.findStuByStuuid(order);
        if (result == null) {
            //添加单位选择信息
            boolean flag = orderService.addOrder(order);
            if (flag) { //添加成功
                mv.addObject("msg", "温馨提示：申请成功");
            } else {//失败，并给出错误提示
                mv.addObject("msg", "温馨提示：未知异常");
            }
        } else {
            mv.addObject("msg", "温馨提示：已经申请一个岗位，不能再次申请");
        }
        return mv;
    }

    /**
     * 删除订单
     *
     * @param oid
     * @param model
     * @return
     */
    @RequestMapping("/delete_order")
    public String deleteOrder(String oid, Model model) {
        //需要有一个能够帮助我们实现订单信息删除的业务逻辑
        boolean flag = orderService.deleteOrder(oid);
        if (flag) {//成功
            return "redirect:/order_list";
        } else {//失败，并给出错误提示
            model.addAttribute("msg", "出现未知问题");
            return "web/failure";
        }
    }

    /**
     * 修改订单数据
     *
     * @param searchOrder
     * @return
     */
//    @RequestMapping("/update_order")
//    public ModelAndView updateOrder(Order searchOrder) {
//        ModelAndView modelAndView;
//        //查询时返回修改的订单数据
//        if (searchOrder.getOrdername() == null) {
//            modelAndView = new ModelAndView("web/order/update_order");
//            Order order = orderService.findByOid(searchOrder);
//            //查询角色为用工单位的所有用户
//            User user = new User();
//            user.setRid(2);
//            List<User> userList = userService.findUserList(user);
//            modelAndView.addObject("userList", userList);
//            modelAndView.addObject("order", order);
//        } else {
//            //否则便是修改数据
//            modelAndView = new ModelAndView();
//            searchOrder.setApplyOrder(1);
//            boolean flag = orderService.updateOrder(searchOrder);
//            if (flag) {//成功，返回订单列表
//                modelAndView.setViewName("forward:/order_list");
//                modelAndView.addObject("msg", "成功");
//            } else {//失败，转发原来界面，并给出错误提示
//                modelAndView.setViewName("forward:/update_order");
//                modelAndView.addObject("msg", "错误");
//            }
//        }
//        return modelAndView;
//    }

//    /**
//     * 审核订单是否通过
//     * @param searchOrder
//     * @return
//     */
//    @RequestMapping("/update_order_apply")
//    public ModelAndView updateOrderApply(Order searchOrder) {
//        ModelAndView modelAndView;
//         //修改是否通过信息
//            modelAndView = new ModelAndView();
//            boolean flag = orderService.updateOrder(searchOrder);
//            if (flag) {//成功，返回订单列表
//                modelAndView.setViewName("forward:/order_list");
//                modelAndView.addObject("msg", "成功");
//            } else {//失败，转发原来界面，并给出错误提示
//                modelAndView.setViewName("forward:/update_order");
//                modelAndView.addObject("msg", "错误");
//            }
//        return modelAndView;
//
//    }

    /**
     * 显示查询所有订单列表
     *
     * @param searchOrder
     * @return
     */
    @RequestMapping("/order_list")
    public ModelAndView toList(Post post, Order searchOrder, HttpSession session) {
        searchOrder.setStuuid(session.getAttribute("uid").toString());
        ModelAndView modelAndView = new ModelAndView("web/order/order_list");

        List<Order> orderList = orderService.findOrderList(searchOrder);
        if(orderList.size()==0){
            modelAndView.setViewName("web/nodata");
            return modelAndView;
        }
        List<Post> postList = new ArrayList<>();
        for (Order oo : orderList) {
            Post p = new Post();
            p.setPid(oo.getPid());
            Post pResult = postService.findPostByPid(p);
            postList.add(pResult);

            //设置状态
            oo.setState(oo.getFinishapplywork() + oo.getFinishwork() + oo.getFinishsalary());

        }
        modelAndView.addObject("postList", postList);
        modelAndView.addObject("orderList", orderList);
        return modelAndView;
    }


    /**
     * 修改订单数据
     *
     * @param searchOrder
     * @return
     */
    @RequestMapping("/update_order_en")
    public ModelAndView updateOrderEn(Order searchOrder) {
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("forward:/order_list_work");
        if (searchOrder.getFinishapplywork() != null) {
            //查询时返回修改的订单数据
            if (searchOrder.getFinishapplywork() == 1 || searchOrder.getFinishapplywork() == 5) {
                //修改申请数据
                Order order = orderService.findByOid(searchOrder);
                order.setFinishapplywork(searchOrder.getFinishapplywork());
                String flag = orderService.updateOrder(order);
                if (flag=="1") {//成功，返回订单列表
                    modelAndView.addObject("msg", "操作成功");
                } else {//失败，转发原来界面，并给出错误提示
                    modelAndView.addObject("msg", "错误");
                }
            } else {
                modelAndView.addObject("msg", "未知异常");
            }
        } else if(searchOrder.getFinishwork()!=null){

            if (searchOrder.getFinishwork() == 1) {
                //修改工作状态为完成工作
                Order order = orderService.findByOid(searchOrder);
                order.setFinishwork(searchOrder.getFinishwork());
                String flag = orderService.updateOrder(order);
                if (flag=="1") {//成功，返回订单列表
                    modelAndView.addObject("msg", "操作成功");
                } else {//失败，转发原来界面，并给出错误提示
                    modelAndView.addObject("msg", "错误");
                }
            } else {
                modelAndView.addObject("msg", "未知异常");
            }
        }else{

            if (searchOrder.getFinishsalary() == 1) {
                //发放酬劳
                Order order = orderService.findByOid(searchOrder);
                order.setFinishsalary(searchOrder.getFinishsalary());

                String flag = orderService.updateOrder(order);
                if (flag=="1") {//成功，返回订单列表
                    modelAndView.addObject("msg", "操作成功");
                } else {//失败，转发原来界面，并给出错误提示
                    modelAndView.addObject("msg", "操作失败"+flag);
                }
            } else {
                modelAndView.addObject("msg", "操作失败!");
            }
        }
        return modelAndView;
    }

    @RequestMapping("/update_order_admin")
    public ModelAndView updateOrderAdmin(Order searchOrder) {
        ModelAndView modelAndView=new ModelAndView();
        Order orderPid = orderService.findByOid(searchOrder);

        Post p=new Post();
        p.setPid(orderPid.getPid());
        Post resultPost= postService.findPostByPid(p);
        //如果已经满员
        modelAndView.setViewName("forward:/order_list_admin");
        //查询时返回修改的订单数据
        if (searchOrder.getFinishapplywork() == 2 || searchOrder.getFinishapplywork() == 5) {
            //修改申请数据
            Order order = orderService.findByOid(searchOrder);
            order.setFinishapplywork(searchOrder.getFinishapplywork());
            boolean flag = orderService.updateOrderAdmin(order, searchOrder.getFinishapplywork());
            if (flag) {//成功，返回订单列表
                modelAndView.addObject("msg", "操作成功");
            } else {//失败，转发原来界面，并给出错误提示
                modelAndView.addObject("msg", "错误");
            }
        } else {
            modelAndView.addObject("msg", "未知异常");
        }
        if(Objects.equals(resultPost.getTotalNumber(), resultPost.getHireNumber())){
            modelAndView.addObject("msg", "工作已满员");

        }
        return modelAndView;
    }


    @RequestMapping("/order_list_en")
    public ModelAndView toListEn(Post post, Order searchOrder, HttpSession session) {

        searchOrder.setEntuid(session.getAttribute("uid").toString());
        ModelAndView modelAndView = new ModelAndView("web/order/order_list_en");
        //查询所有申请列表，通过自己的id
        List<Order> orderList = orderService.findOrderListByEntuid(searchOrder);
        if(orderList.size()==0){
            modelAndView.setViewName("web/nodata");
            return modelAndView;
        }
        List<User> userList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        for (Order oo : orderList) {
            Post p = new Post();
            p.setPid(oo.getPid());
            Post pResult = postService.findPostByPid(p);
            //获取学生信息
            User uu = new User();
            uu.setUid(oo.getStuuid());
            User user = userService.findByUid(uu);
            if (user == null) {
                return null;
            } else {
                user.setPassword(null);
                user.setSalt(null);
                userList.add(user);
                postList.add(pResult);
            }
            oo.setState(oo.getFinishapplywork() + oo.getFinishwork() + oo.getFinishsalary());
        }
        modelAndView.addObject("userList", userList);
        modelAndView.addObject("postList", postList);
        modelAndView.addObject("orderList", orderList);
        //设置状态
        return modelAndView;
    }

    @RequestMapping("/order_list_admin")
    public ModelAndView toListAdmin(Post post, Order searchOrder, HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("web/order/order_list_en");
        //查询所有申请列表，通过自己的id
        List<Order> orderList = orderService.findOrderListByEntuid(searchOrder);
        if(orderList.size()==0){
            modelAndView.setViewName("web/nodata");
            return modelAndView;
        }
        List<User> userList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        for (Order oo : orderList) {
            Post p = new Post();
            p.setPid(oo.getPid());
            Post pResult = postService.findPostByPid(p);
            //获取学生信息
            User uu = new User();
            uu.setUid(oo.getStuuid());
            User user = userService.findByUid(uu);
            if (user == null) {
                return null;
            } else {
                user.setPassword(null);
                user.setSalt(null);
                userList.add(user);
                postList.add(pResult);
            }
            //设置状态
            oo.setState(oo.getFinishapplywork() + oo.getFinishwork() + oo.getFinishsalary());
        }
        modelAndView.addObject("userList", userList);
        modelAndView.addObject("postList", postList);
        modelAndView.addObject("orderList", orderList);

        return modelAndView;
    }


    /**
     * 订单详情
     *
     * @param searchOrder
     * @return
     */
    @RequestMapping("/detail_order_en")
    public ModelAndView detailOrder(Order searchOrder) {
        ModelAndView modelAndView = new ModelAndView("web/order/detail_order");
        Order order = orderService.findByOid(searchOrder);
        Post p = new Post();
        p.setPid(order.getPid());
        Post post = postService.findPostByPid(p);
        User u = new User();
        u.setUid(order.getStuuid());
        User userstu = userService.findByUid(u);
        modelAndView.addObject("userstu", userstu);
        modelAndView.addObject("post", post);
        order.setState(order.getFinishapplywork() + order.getFinishwork() + order.getFinishsalary());
        modelAndView.addObject("order", order);
        return modelAndView;
    }

    @RequestMapping("/order_list_work")
    public ModelAndView orderListWork(Post post, Order searchOrder, HttpSession session) {
        //生成表
        searchOrder.setEntuid(session.getAttribute("uid").toString());
        ModelAndView modelAndView = new ModelAndView("web/order/order_work_list");
        //查询所有申请列表，通过自己的id
        List<Order> orderList = orderService.findOrderListByEntuidWork(searchOrder);
        if(orderList.size()==0){
            modelAndView.setViewName("web/nodata");
            return modelAndView;
        }
        List<User> userList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        for (Order oo : orderList) {
            Post p = new Post();
            p.setPid(oo.getPid());
            Post pResult = postService.findPostByPid(p);
            //获取学生信息
            User uu = new User();
            uu.setUid(oo.getStuuid());
            User user = userService.findByUid(uu);
            if (user == null) {
                return null;
            } else {
                user.setPassword(null);
                user.setSalt(null);
                userList.add(user);
                postList.add(pResult);
            }
            oo.setState(oo.getFinishapplywork() + oo.getFinishwork() + oo.getFinishsalary());
        }
        modelAndView.addObject("userList", userList);
        modelAndView.addObject("postList", postList);
        modelAndView.addObject("orderList", orderList);
        //设置状态
        return modelAndView;

    }

}
