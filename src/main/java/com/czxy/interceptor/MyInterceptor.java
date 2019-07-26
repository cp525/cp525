package com.czxy.interceptor;

import com.czxy.domain.User;
import com.czxy.servie.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String requestURI = request.getRequestURI();

        System.out.println(requestURI);

        if (requestURI.contains("login") || requestURI.contains("/css") || requestURI.contains("/js") || requestURI.contains("/img") || requestURI.contains("/fonts")) {
            //放行
            return true;
        } else {
            // 查看session中是否 有 loginU
            User loginU = (User) request.getSession().getAttribute("loginU");
            if (loginU != null) {
                //已经登录 放行
                return true;
            } else {
                //判断 是否勾选了 自动登录  如果勾选了 自动登录,  否则 一律跳转到登陆页面
                Cookie[] cookies = request.getCookies();

                if (cookies != null) {
                    User user = new User();
                    boolean auto = false;
                    for (Cookie ck : cookies) {
                        if (ck.getName().equals("username")) {
                            user.setUsername(ck.getValue());
                        }
                        if (ck.getName().equals("password")) {
                            user.setPassword(ck.getValue());
                        }

                        if (ck.getName().equals("autologin")) {
                            auto = true;
                        }
                    }

                    if (auto) {
                        System.out.println("自动了:" + user);
                        //自动登录
                        User loginu = userService.login(user);
                        if (loginu != null) {
                            request.getSession().setAttribute("loginU", loginu);
                            //登录成功 放行
                            return true;
                        } else {
                            //跳转到登陆页面
                            response.sendRedirect("/login.html");
                            return false;
                        }

                    } else {
                        //跳转到登陆页面
                        response.sendRedirect("/login.html");
                        return false;
                    }
                } else {
                    //跳转到登陆页面
                    response.sendRedirect("/login.html");
                    return false;
                }
            }
        }
    }
}
