package com.ning.bishe01.config;



import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.ning.bishe01.realm.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

//    /**
//     * 配置 Shiro 过滤器
//     */
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/**", "authc");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//
//        return shiroFilterFactoryBean;
//    }
//
//    /**
//     * 配置 SecurityManager
//     * @return
//     */
//    @Bean
//    public DefaultWebSecurityManager securityManager(Realm realm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(realm);
//
//        return securityManager;
//    }

//    /**
//     * 配置 Realm
//     */
//    @Bean
//    public Realm realm() {
//        return new IniRealm("classpath:shiro.ini");
//    }

    /**
     * //过滤器工厂，Shiro的基本运行机制是开发者定制规则，Shiro去执行，具体的执行操作就是ShiroFilterFactoryBean创建的一个个Filter对象来完成。
     *
     * 1.认证过滤器：
     * anon无需认证
     * authc必须认证
     * authcBasic需要通过HTTPBasic认证
     * user不一定通过认证，只要曾经被Shiro记录即可，比如记住我；
     * 2.授权过滤器
     * perms:必须拥有某个权限才能访问
     * role:必须拥有某个角色才能访问
     * port:请求的端口必须是指定值才可以
     * rest:请求必须基于RESTful、POST、PUT、GET、DELETE
     * ssl:必须是安全的url请求，且协议是HTTPS
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){

        ShiroFilterFactoryBean factoryBean=new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(defaultWebSecurityManager);
//        factoryBean.setUnauthorizedUrl("/404");
        //权限设置
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/", "anon");//无需认证
        filterChainDefinitionMap.put("/login", "anon");//无需认证
        filterChainDefinitionMap.put("/css/**", "anon");//无需认证
        filterChainDefinitionMap.put("/js/**", "anon");//无需认证
        filterChainDefinitionMap.put("/fonts/**", "anon");//无需认证
        filterChainDefinitionMap.put("/images/**", "anon");//无需认证
        filterChainDefinitionMap.put("/main.html", "authc");//authc必须认证
        filterChainDefinitionMap.put("/**", "authc");//authc必须认证
        filterChainDefinitionMap.put("/faceLog.html", "anon");
        filterChainDefinitionMap.put("/faceLogin", "anon");
        filterChainDefinitionMap.put("/**", "user");//rememberMe
//        filterChainDefinitionMap.put("/fail", "roles[管理员]");//role:必须拥有某个角色才能访问
//        filterChainDefinitionMap.put("/", "roles[administrator]");//role:必须拥有某个角色才能访问
//        filterChainDefinitionMap.put("/manage", "perms[manage]");//perms:必须拥有某个权限才能访问
//        filterChainDefinitionMap.put("/administrator", "roles[administrator]");//role:必须拥有某个角色才能访问

        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        factoryBean.setLoginUrl("/");
        return factoryBean;
    }




    //安全管理器，开发者自定义的Realm需要注入到DefaultWebSecurityManager中进行管理才能生效。
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        //设置rememberMe管理器
        manager.setRememberMeManager(rememberMeManager());
        return manager;
    }
    //cookie属性设置
    public SimpleCookie remeberMeCookie(){
        SimpleCookie cookie=new SimpleCookie("rememberMe");
        //设置跨域
        //cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30*24*60*60);//

        return cookie;
    }
    //创建Shiro的cookie管理对象
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        cookieRememberMeManager.setCipherKey("1234567890321654".getBytes());
        return cookieRememberMeManager;

    }
//    //配置shiro内置过滤拦截器拦截范围
//    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition(){
//        DefaultShiroFilterChainDefinition definition=new DefaultShiroFilterChainDefinition();
//        definition.addPathDefinition("/","authc");
//        return definition;
//    }





    //开发者自定义模块，根据项目需求，验证和权限的逻辑全写在Realm中
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
//        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }
    //配置方言
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
    //开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");
//        return hashedCredentialsMatcher;
//    }
//    /**
//     * 开启aop注解支持
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
}