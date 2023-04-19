package com.ning.bishe01.util;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ControllerAdvice注解的作用：是一个Controller增强器，可对controller中被@RequestMapping注解的方法加一些逻辑处理，最常用的就是异常处理；【三种使用场景】全局异常处理。全局数据绑定，全局数据预处理
 * @Order 注解@Order或者接口Ordered的作用是定义SpringIOC容器中Bean的执行顺序的优先级，而不是定义Bean的加载顺序，Bean的加载顺序不受@Order或Ordered接口的影响；
 * @ExceptionHandler 统一异常处理
 * Shiro中setUnauthorizedUrl"/403"不生效，无法跳转指定页面的解决办法,这是因为shiro源代码中判断了filter是否为AuthorizationFilter，只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，所以unauthorizedUrl设置后不起作用。
 */
@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AuthorizationException.class)
    public String handleAuthorizationException() {
        return "/404";
    }
}
//下面也可以
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(UnauthorizedException.class)
//    public ModelAndView permission(){
//        //这个路径填写需要去掉mvc配置的路径
//        ModelAndView modelAndView = new ModelAndView("/404");
//        return modelAndView;
//    }
//}