package com.ning.bishe01.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ning.bishe01.entity.Order;
import com.ning.bishe01.entity.Post;
import com.ning.bishe01.entity.User;
import com.ning.bishe01.service.IOrderService;
import com.ning.bishe01.util.JsonResult;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ning.bishe01.service.IPostService;
import com.ning.bishe01.service.IUserService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class PostController extends BaseController {
    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    /**
     * 管理员添加岗位
     *
     * @param post
     * @param model
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/add_post")
    public String addPost(Post post, Model model) {
        //添加单位选择信息
        User user = new User();
        user.setRid(2);
        List<User> userList = userService.findUserList(user);
        model.addAttribute("userList", userList);
        if (post.getPid() != null) {
            boolean flag = postService.addPost(post);
            if (flag) { //添加成功
                model.addAttribute("msg", "添加成功");
            } else {//失败，并给出错误提示
                model.addAttribute("msg", "岗位名已被占用");
            }
        }
        return "web/post/add_post";
    }

    /**
     * 删除岗位
     *
     * @param pid
     * @param model
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/delete_post")
    public String deletePost(String pid, Model model) {
        //需要有一个能够帮助我们实现岗位信息删除的业务逻辑
        boolean flag = postService.deletePost(pid);
        if (flag) {//成功
            return "forward:/post_list";
        } else {//失败，并给出错误提示
            model.addAttribute("msg", "出现未知问题");
            return "web/failure";
        }
    }

    /**
     * 修改岗位数据
     *
     * @param searchPost
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/update_post")
    public ModelAndView updatePost(Post searchPost) {
        ModelAndView modelAndView;
//修改信息时
        //查询时postname=null返回修改的岗位数据
        if (searchPost.getPostname() == null) {
            modelAndView = new ModelAndView("web/post/update_post");
            Post post = postService.findByPid(searchPost);
            //查询角色为用工单位的所有用户
            User user = new User();
            user.setRid(2);
            List<User> userList = userService.findUserList(user);
            modelAndView.addObject("userList", userList);
            modelAndView.addObject("post", post);
        } else {
            //否则便是修改数据
            modelAndView = new ModelAndView();
            searchPost.setApplyPost(1);
            boolean flag = postService.updatePost(searchPost);
            if (flag) {//成功，返回岗位列表
                modelAndView.setViewName("forward:/post_list");
                modelAndView.addObject("msg", "成功");
            } else {//失败，转发原来界面，并给出错误提示
                modelAndView.setViewName("forward:/update_post");
                modelAndView.addObject("msg", "错误");
            }
        }
        return modelAndView;
    }

    /**
     * 审核岗位是否通过
     *
     * @param searchPost
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/update_post_apply")
    public ModelAndView updatePostApply(Post searchPost) {
        ModelAndView modelAndView;
        //修改是否通过信息
        modelAndView = new ModelAndView();
        boolean flag = postService.updatePost(searchPost);
        if (flag) {//成功，返回岗位列表
            modelAndView.setViewName("forward:/post_list");
            modelAndView.addObject("msg", "成功");
        } else {//失败，转发原来界面，并给出错误提示
            modelAndView.setViewName("forward:/update_post");
            modelAndView.addObject("msg", "错误");
        }
        return modelAndView;

    }

    /**
     * 显示查询所有岗位列表
     *
     * @param searchPost
     * @return
     */
    @PostMapping("/post_list")
    public ModelAndView toList(Post searchPost) {
        ModelAndView modelAndView = new ModelAndView("web/post/post_list");
        //更改成功设置uid=null返回列表时不会查询（直接可以在xml去掉if语句）
        searchPost.setUid(null);
        searchPost.setApplyPost(1);
        List<Post> postList = postService.findPostList(searchPost);
        PageInfo<Post> pageInfo = new PageInfo<>(postList);
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("searchPost", searchPost);
        //将过滤条件传递到页面资源上去
        return modelAndView;
    }

    /**
     * 显示查询所有岗位申请列表
     *
     * @param searchPost
     * @return
     */
    @RequiresRoles("管理员")
    @RequestMapping("/post_list_apply")
    public ModelAndView toListApply(Post searchPost) {
        ModelAndView modelAndView = new ModelAndView("web/post/post_list_apply");
        //更改成功设置uid=null返回列表时不会查询（直接可以在xml去掉if语句）
//        searchPost.setUid(null);
        searchPost.setApplyPost(0);
        List<Post> postList = postService.findPostList(searchPost);
        PageInfo<Post> pageInfo = new PageInfo<>(postList);
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("searchPost", searchPost);
        //将过滤条件传递到页面资源上去
        return modelAndView;
    }

    /**
     * 岗位详情
     *
     * @param searchPost
     * @return
     */
    @RequestMapping("/detail_post")
    public ModelAndView detailPost(Post searchPost) {
        ModelAndView modelAndView = new ModelAndView("web/post/detail_post");
        Post post = postService.findByPid(searchPost);
        modelAndView.addObject("post", post);
        return modelAndView;
    }


    /**
     * 显示查询单位拥有的岗位列表
     *
     * @param searchPost
     * @return
     */
    @RequiresRoles("用工单位")
    @PostMapping("/post_list_en")
    public ModelAndView toList_en(@PathParam("uid") String uid, Post searchPost, HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("web/post/post_list_en");
        //进入查看申请进度
        if(uid!=null){
            searchPost.setApplyPost(0);
            List<Post> postList = postService.findPostList(searchPost);
            PageInfo<Post> pageInfo = new PageInfo<>(postList);
            modelAndView.addObject("pageInfo", pageInfo);
            modelAndView.addObject("msgapply", "申请中");
        }else{//列表
            searchPost.setUid(session.getAttribute("uid").toString());
            //设置已通过申请的岗位
            searchPost.setApplyPost(1);
            List<Post> postList = postService.findPostList(searchPost);


            modelAndView.addObject("postList", postList);
            List<Integer> olist = new ArrayList<>();
            int i=0;

            for(Post p:postList){
                Order order=new Order();
                order.setPid(p.getPid());
                Order result=  orderService.findOrderByPid(order);
                if(result==null){
                    olist.add(i, 1);//无人申请，可删
                }else {
                    olist.add(i, 0);
                }
                i++;
            }
            modelAndView.addObject("olist", olist);

            //将过滤条件传递到页面资源上去
        }
        modelAndView.addObject("searchPost", searchPost);
        return modelAndView;


    }

    /**
     * 删除岗位
     *
     * @param pid
     * @param model
     * @return
     */
    @RequiresRoles("用工单位")
    @RequestMapping("/delete_post_en")
    public String deletePost_en(String pid, Model model, HttpSession session) {


        String uid = postService.findUidByPid(pid);
        if (session.getAttribute("uid").toString().equals(uid)) {
            //需要有一个能够帮助我们实现岗位信息删除的业务逻辑
            boolean flag = postService.deletePost(pid);
            if (flag) {//成功
                model.addAttribute("msg", "删除成功");
                return "forward:/post_list_en";
            } else {//失败，并给出错误提示
                model.addAttribute("msg", "出现未知问题");
                return "forward:/post_list_en";
            }
        } else {
            model.addAttribute("msg", "出现未知问题");
            return "forward:/post_list_en";
        }
    }

    /**
     * 岗位详情
     *
     * @param searchPost
     * @return
     */
    @RequestMapping("/detail_post_en")
    public ModelAndView detailPostEn(Post searchPost) {
        ModelAndView modelAndView = new ModelAndView("web/post/detail_post_en");
        Post post = postService.findByPid(searchPost);
        modelAndView.addObject("post", post);
        return modelAndView;
    }


    /**
     * 用工单位申报岗位
     *
     * @param post
     * @param model
     * @return
     */
    @RequiresRoles("用工单位")
    @RequestMapping("/add_post_en")
    public String addPostEn(Post post, Model model, HttpSession session) {
        User user = new User();
        user.setRid(2);
        post.setUid(session.getAttribute("uid").toString());
        if (post.getPid() != null) {
            boolean flag = postService.addPost_en(post);
            if (flag) { //添加成功
                model.addAttribute("msg", "添加成功");
            } else {//失败，并给出错误提示
                model.addAttribute("msg", "岗位id已被占用");
            }
        }
        return "web/post/add_post_en";
    }


}
