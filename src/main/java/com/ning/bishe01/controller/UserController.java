package com.ning.bishe01.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ning.bishe01.entity.UData;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.entity.Role;
import com.ning.bishe01.realm.UserRealm;
import com.ning.bishe01.service.IUDataService;
import com.ning.bishe01.util.FaceUtil;
import com.ning.bishe01.util.JsonResult;
import net.sf.json.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ning.bishe01.service.IUserService;
import com.ning.bishe01.service.IRoleService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUDataService uDataService;
//    @GetMapping("/{url}")
//    public String redirect(@PathVariable("url") String url) {
//        return url;
//    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView();
        //在这里执行退出系统前需要清空的数据
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        mv.setViewName("forward:/");
        return mv;
    }

    /**
     * 登录
     *
     * @param user
     * @param session 登录成功保存到session中
     * @return
     */
    @RequestMapping("/login")
//    @PostMapping("/login")
    public ModelAndView login(User user, @RequestParam(defaultValue = "false") boolean rememberMe, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        User data = userService.findByUid(user);
        List<Role> roleList = roleService.findRoleList();
        mv.addObject("roleList", roleList);
        try {
            if (!Objects.equals(data.getRid(), user.getRid())) {
                mv.setViewName("/login.html");
                String msg = "角色有误！";
                mv.addObject("msg", msg);
                return mv;
            }
            //Subject:Shiro的一个抽象概念，包含用户信息。
            Subject subject = SecurityUtils.getSubject();
            //UsernamePasswordToken:Shiro用来封装用户登陆信息，使用用户的登录信息来创建令牌Token
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUid(), user.getPassword(), rememberMe);
            subject.login(token);
            User user1 = (User) subject.getPrincipal();
            subject.getSession().setAttribute("user", user1);
            mv.setViewName("redirect:/main.html");
            session.setAttribute("uid", data.getUid());
            session.setAttribute("rolename", data.getRole().getRolename());
            session.setAttribute("username", data.getUsername());
            session.setAttribute("password", user.getPassword());
        } catch (Exception e) {
            mv.setViewName("/login.html");
            String msg = "请输入正确的账号和密码！";
            mv.addObject("msg", msg);
        }
        return mv;
    }

    @GetMapping("/main.html")
    public String main() {
        return "main";
    }

    /**
     * 添加用户
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresRoles("管理员")
    @PostMapping("/add_user")
    public String addUser(User user, Model model) {
        //添加角色选择信息
        List<Role> roleList = roleService.findRoleList();
        model.addAttribute("roleList", roleList);
        if (user.getUid() != null) {
            boolean flag = userService.addUser(user);
            if (flag) { //添加成功
                model.addAttribute("msg", "添加成功");
            } else {//失败，并给出错误提示
                model.addAttribute("msg", "用户名已被占用");
            }
        }
        return "web/user/add_user";
    }

    /**
     * 删除用户
     *
     * @param uid
     * @param model
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/delete_user")
    public String deleteUser(String uid, Model model) {
        //需要有一个能够帮助我们实现用户信息删除的业务逻辑
        boolean flag = userService.deleteUser(uid);
        if (flag) {//成功
            return "forward:/user_list";
        } else {//失败，并给出错误提示
            model.addAttribute("msg", "出现未知问题");
            return "web/fail";
        }
    }

    /**
     * 修改用户数据
     *
     * @param searchUser
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/update_user")
    public ModelAndView updateUser(User searchUser) {
        //查询时username=null返回修改的用户数据
        ModelAndView modelAndView;
        if (searchUser.getUsername() == null) {
            modelAndView = new ModelAndView("web/user/update_user");
            User user = userService.findByUid(searchUser);
            List<Role> roleList = roleService.findRoleList();
            modelAndView.addObject("roleList", roleList);
            modelAndView.addObject("user", user);
        } else {
            //否则便是修改数据
            modelAndView = new ModelAndView();
            boolean flag = userService.updateUser(searchUser);
            if (flag) {//成功，返回用户列表
                modelAndView.setViewName("forward:/user_list");
                modelAndView.addObject("msg", "成功");
            } else {//失败，转发原来界面，并给出错误提示
                modelAndView.setViewName("forward:/update_user");
                modelAndView.addObject("msg", "错误");
            }
        }
        return modelAndView;
    }

    /**
     * 个人中心修改
     *
     * @param user
     * @param uData
     * @return
     */
    @RequestMapping("/update_person")
    public ModelAndView updatePerson(User user, UData uData, HttpSession session) {
        //查询时username=null返回修改的用户数据
        ModelAndView modelAndView = new ModelAndView();
        boolean flag = uDataService.updateUData(uData, user);
        if (flag) {//成功，返回用户列表
            session.setAttribute("username", user.getUsername());
            modelAndView.setViewName("forward:/user_data");
            modelAndView.addObject("msg", "保存成功");
        } else {//失败，转发原来界面，并给出错误提示
            modelAndView.setViewName("forward:/user_data");
            modelAndView.addObject("msg", "保存失败");
        }
        return modelAndView;
    }

    /**
     * 密码修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/update_password")
    public ModelAndView updatePassword(@PathParam("oldpassword") String oldpassword, User user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (user.getUsername() == null) {
            modelAndView.setViewName("web/udata/updatePassword");
            return modelAndView;
        }
        //人脸登录时，登录验证一下，成功则存储到session中
        try {
            if (session.getAttribute("password") == null) {
                Subject subject = SecurityUtils.getSubject();
                oldpassword = null;
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUid(),oldpassword);
                subject.login(token);
                session.setAttribute("password", user.getPassword());

            }
        } catch (Exception e) {
            modelAndView.setViewName("web/udata/updatePassword");
            modelAndView.addObject("msg", "密码错误");
        }
        if (oldpassword != null) {
            if (!oldpassword.equals(session.getAttribute("password"))) {
                modelAndView.setViewName("web/udata/updatePassword");
                modelAndView.addObject("msg", "密码错误");
                return modelAndView;
            }
        }
        boolean flag = userService.updateUser(user);
        if (flag) {//成功，返回用户列表
            modelAndView.setViewName("forward:/logout");
            modelAndView.addObject("msg", "密码更改成功，账户过期，请重新登录！");
        } else {//失败，转发原来界面，并给出错误提示
            modelAndView.setViewName("forward:web/udata/updatePassword");
            modelAndView.addObject("msg", "保存失败");
        }
        return modelAndView;
    }


    /**
     * 显示查询所有用户列表
     *
     * @param searchUser
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/user_list")
    public ModelAndView toList(User searchUser) {
        ModelAndView modelAndView = new ModelAndView("web/user/user_list");
        List<User> userList = userService.findUserList(searchUser);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("searchUser", searchUser);
        return modelAndView;
    }

    /**
     * 用户详情
     *
     * @param searchUser
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/detail_user")
    public ModelAndView detailUser(User searchUser) {
        ModelAndView modelAndView = new ModelAndView("web/user/detail_user");
        User user = userService.findByUid(searchUser);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    //测试
    @RequiresPermissions("index")
    @RequestMapping("/test")
    public ModelAndView test(User searchUser, Integer currentPage) {
//        ModelAndView modelAndView = new ModelAndView("web/user/userListTest");
        ModelAndView modelAndView = new ModelAndView("web/user/dynamic_table");
        List<User> userList = userService.findUserList(searchUser);
        //使用PageInfo进行对分页结果的包装
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        //将得到的分页结果传递到下一个资源上去
        modelAndView.addObject("pageInfo", pageInfo);
        //将过滤条件传递到页面资源上去
        modelAndView.addObject("searchUser", searchUser);

        return modelAndView;
    }

    @RequestMapping("/noauth2")
    @ResponseBody
    public String noauthorizwd2() {
        return "无权限,无法访问";
    }

    @RequiresRoles("学生")
    @GetMapping("/fail")
    public String fail() {

        return "fail";
    }


    /**
     * 人脸注册页面
     *
     * @return
     */
    @RequestMapping("/faceReg.html")
    public String toFaceReg() {
        return "faceReg";
    }

    /**
     * 人脸登录页面
     *
     * @return
     */
    @RequestMapping("/faceLog.html")
    public String toFaceLog() {
        return "faceLog";
    }

    /**
     * 实现人脸注册
     *
     * @param img
     * @return
     */
    @RequestMapping("/faceRegister")
    @ResponseBody
    public String faceRegister(String img) {
        //Subject:Shiro的一个抽象概念，包含用户信息。
        Subject subject = SecurityUtils.getSubject();
        User user1 = (User) subject.getPrincipal();
        String resp = FaceUtil.faceRegister(img, user1.getUid() + "", "bishe01");
        System.out.println("人脸注册后的结果：" + resp);
        JSONObject jsonObj = JSONObject.fromObject(resp);
        String msg = jsonObj.get("error_msg").toString();
        System.out.println("人脸注册后的error_msg：" + msg);
        return msg;
    }

    /**
     * 实现人脸登录
     *
     * @param img
     * @return
     */
    @RequestMapping("/faceLogin")
    @ResponseBody
    public String faceLogin(String img, HttpSession session) {
        String groupIdList = "bishe01";
        String resp = FaceUtil.faceLogin(img, groupIdList);
        System.out.println("人脸登录后的结果：" + resp);
        JSONObject jsonObj = JSONObject.fromObject(resp);
        String msg = jsonObj.get("error_msg").toString();
        System.out.println("人脸登录后的error_msg：" + msg);
        if ("SUCCESS".equals(msg)) {
            try {
                JSONObject result = JSONObject.fromObject(jsonObj.get("result"));
                JSONArray userList = JSONArray.fromObject(result.get("user_list"));
                JSONObject u = JSONObject.fromObject(userList.get(0));
//            Integer uid = Integer.parseInt((String)u.get("user_id"));
                String uid = (String) u.get("user_id");
                User uu = new User();
                uu.setUid(uid);
                User user = userService.findByUid(uu);
                //Subject:Shiro的一个抽象概念，包含用户信息。
                Subject subject = SecurityUtils.getSubject();
                user.setPassword(null);
                //UsernamePasswordToken:Shiro用来封装用户登陆信息，使用用户的登录信息来创建令牌Token
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUid(), user.getPassword());
                subject.login(token);
                User user1 = (User) subject.getPrincipal();
                subject.getSession().setAttribute("user", user1);
                session.setAttribute("uid", user.getUid());
                session.setAttribute("rolename", user.getRole().getRolename());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("password", null);///////////////////////////////////////////////////修改密码时错误
//            session.setAttribute("SPRING_SECURITY_CONTEXT",context);
            } catch (Exception e) {
                System.err.println("错误" + e);
            }
        }
        return msg;
    }


}
