package com.ning.bishe01.controller;


import com.ning.bishe01.service.ex.*;
import com.ning.bishe01.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/** 控制器类的基类 */
public class BaseController {
    /** 操作成功状态码 */
    public  static final  int OK=200;
    /** 请求处理方法，这个方法的返回值就是需要传递给前端的数据
     * 自动将异常对象传递给此方法的参数上
     * 当前项目产生的异常，被统一拦截到此方法中，这个方法此时就充当的是请求处理方法，方法的返回值直接给到前端
     * */
    @ExceptionHandler(ServiceException.class)//用于统一处理抛出的异常
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException) {
            result.setState(4000);
            result.setMessage("用户名已经被占用");
        } else if (e instanceof InsertException) {
            result.setState(4001);
            result.setMessage("插入出现未知异常");
        }
        else if (e instanceof PasswordnoMatchException) {
            result.setState(4002);
            result.setMessage("密码错误");
        } else if (e instanceof UsernameNofindException) {
            result.setState(4003);
            result.setMessage("未查询到该用户");
        }else if (e instanceof UpdateException) {
            result.setState(4004);
            result.setMessage("更新出现未知异常");
        }else if (e instanceof DeleteException) {
            result.setState(4005);
            result.setMessage("删除出现未知异常");
        }else if (e instanceof RolenameExistException) {
            result.setState(4006);
            result.setMessage("该角色已存在");
        }


        return  result;

    }
    /**
     * 从HttpSession对象中获取uid
     * HttpSession对象
     * @return 当前登录的用户的id
     * session可以保存任何类型数据，而cookie中只能保存String类型
     */
    protected final Integer getUidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }
    /**
     * 从HttpSession对象中获取用户名
     * HttpSession对象
     * @return 当前登录的用户名
     */
    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }
}
