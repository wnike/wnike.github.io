//package com.ning.bishe01.config;
//
//import com.ning.bishe01.service.IUserService;
//import com.ning.bishe01.service.impl.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//@EnableWebSecurity//@EnableXXX开启某功能//标识为spring security 的配置类，并启动，含有@Component，注册到了spring的组件中
//
//public class SecrityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    protected UserServiceImpl userService;
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //解决静态资源被拦截问题
//        web.ignoring().antMatchers("/css/**");
//        web.ignoring().antMatchers("/js/**");
//        web.ignoring().antMatchers("/images/**");
//    }
////授权
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.userDetailsService(userService);
//        http.authorizeRequests()//开启登陆配置
//                //登陆页都可以访问
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()//其他所有请求，只需要登录即可
//                .and()
//                .formLogin()
//                .loginPage("/login.html")//换成我们自定义的登录页面
//                .loginProcessingUrl("/login") //登录请求转发到 /login 要与表单中action转发的url一致
//                .defaultSuccessUrl("/main.html").permitAll()//当认证成功后，跳转到首页
//                .failureUrl("/main.html").permitAll(); //认证失败的处理url，暂时就重定向到登录页
//        http.headers().frameOptions().sameOrigin();
//
////        //开启了注销功能
////        http.logout()
////                .logoutUrl("/login")//指定退出的地址
////                .logoutSuccessUrl("/login.html");//退出成功后跳转的界面//登出操作成功执行之后，请求的路径
////
////
////
////        //关闭跨站伪造请求攻击
////        http.csrf().disable();
////
////        //开启记住我功能,cookie 默认保存2周
////        http.rememberMe();
//    }
//
//
//    //认证
//
//
//    //在这里完成获得数据库中的用户信息
//    //密码一定要加密
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//}
