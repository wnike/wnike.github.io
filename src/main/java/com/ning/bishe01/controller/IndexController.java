package com.ning.bishe01.controller;

import com.ning.bishe01.entity.Role;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.service.IRoleService;
import com.ning.bishe01.service.IUserService;
import com.ning.bishe01.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private IRoleService roleService;
//    @GetMapping({"/",""})
@RequestMapping({"/",""})
    public String index(Model model){
    List<Role> roleList= roleService.findRoleList();
    model.addAttribute("roleList", roleList);
    return  "login";
    }
    @RequestMapping("/404")
    public String noauthorizwd(){
    return "/404";
    }
    @RequestMapping("/maintenance")
    public String maintenance(){
        return "/maintenance";
    }

}