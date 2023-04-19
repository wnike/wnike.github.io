package com.ning.bishe01.realm;


import com.ning.bishe01.entity.User;
import com.ning.bishe01.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    /**
     * 角色的权限信息集合，授权时使用
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登陆的用户信息
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();//String principal=principalCollection.getPrimaryPrincipal().toString();
        //设置角色,用set不用list因为set防止重复
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole().getRolename());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        info.addRole(user.getRole().getRolename());
        //设置权限
        info.addStringPermission(user.getRole().getRolename());//info.addRole("admin");//设置角色
        return info;
    }

    /**
     * 用户的角色信息集合，认证时使用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User uu = new User();
        uu.setUid(token.getUsername());
        User user = userService.findByUid(uu);
        //人脸登录时密码为空，不用判断
        if (token.getPassword() != null) {
            String salt = user.getSalt();
            String tokenPassword = String.valueOf(token.getPassword());
            String password = tokenPassword;
            for (int i = 0; i < 3; i++) {
                password = DigestUtils.md5DigestAsHex((salt + password +
                        salt).getBytes()).toUpperCase();
            }
            token.setPassword(password.toCharArray());

        }else{
            token.setPassword(user.getPassword().toCharArray());
        }
            if (user != null) {
                //第一个参数：传入的都是com.java.entity包下的User类的user对象。
                //第二个参数是从数据库中获取的password 和token（filter中登录时生成的）中的password做对比，如果验证成功，最终这里返回的信息的值与传入的第一个字段的值相同
                //第三个参数，盐–用于加密密码对比。 若不需要，则可以设置为空 “ ”
                //
                //第四个参数：当前realm的名字。
                return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
            }

        return null;
    }
}
