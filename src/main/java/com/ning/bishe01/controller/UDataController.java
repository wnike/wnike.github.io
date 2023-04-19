package com.ning.bishe01.controller;


import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.service.IRoleService;
import com.ning.bishe01.service.IUDataService;
import com.ning.bishe01.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;

@Controller

public class UDataController {
    @Autowired
    private IUDataService uDataService;
    @Autowired
    private IUserService userService;
//

    @PostMapping("/user_data")
    public ModelAndView userData(String uid) {

        UData uData=new UData();
        uData.setUid(uid);
        uData=uDataService.findUDataByUid(uData);
        User uu=new User();
        uu.setUid(uid);
        User user=userService.findByUid(uu);
        if(uData==null){
            return null;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("uData", uData);
        mv.addObject("user",user);
        mv.setViewName("web/udata/user_data.html");
        return mv;
    }

    /**
     * 充值
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/add_money")
    public ModelAndView addMoney(@PathParam("money") String money, UData uData) {

        ModelAndView modelAndView = new ModelAndView();
        if(uData.getUid()==null){
            modelAndView.setViewName("web/uData/add_money");
            return  modelAndView;
        }
        BigDecimal usersalary = new BigDecimal(money);
        uData.setUsersalary(usersalary);
        boolean flag = uDataService.addUDataMoney(uData);
        if (flag) {//成功，返回用户列表
            uData.setUid(null);
            modelAndView.setViewName("web/uData/add_money");
            modelAndView.addObject("msg", "充值成功");
        } else {//失败，转发原来界面，并给出错误提示
            modelAndView.setViewName("web/uData/add_money");
            modelAndView.addObject("msg", "充值失败");
        }
        return modelAndView;
    }

    @PostMapping("/user_data2")
    public ModelAndView userData2(String uid) {

        UData uData=new UData();
        uData.setUid(uid);
        uData=uDataService.findUDataByUid(uData);
        User uu=new User();
        uu.setUid(uid);
        User user=userService.findByUid(uu);
        if(uData==null){
            return null;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("uData", uData);
        mv.addObject("user",user);
        mv.setViewName("web/udata/updatePassword.html");
        return mv;
    }

}
