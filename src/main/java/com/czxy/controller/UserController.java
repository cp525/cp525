package com.czxy.controller;

import com.czxy.domain.User;
import com.czxy.servie.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("loginName")
    public ResponseEntity<User> getLoginName(HttpSession session){

        User loginU = (User) session.getAttribute("loginU");
        if(loginU !=null){
            return ResponseEntity.ok(loginU);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }
    @PostMapping("login2")
    public ResponseEntity<String> login2(String autologin, User user, HttpServletResponse response, HttpSession session){

        System.out.println(autologin);

        System.out.println(user);

        User loginU = userService.login(user);

        if(loginU !=null){
            session.setAttribute("loginU", user);
        }else{
            return new ResponseEntity<>("用户名或密码错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Cookie ck1 = new Cookie("autologin", "yes");
        Cookie ck2 = new Cookie("username", user.getUsername());
        Cookie ck3 = new Cookie("password", user.getPassword());

        ck1.setPath("/");
        ck2.setPath("/");
        ck3.setPath("/");

        if("yes".equals(autologin)){
            //勾选了记住我  存活时间 60秒
            //创建 仨cookie 分别存储 用户名 密码 勾选状态 给前端
            ck1.setMaxAge(60);
            ck2.setMaxAge(60);
            ck3.setMaxAge(60);
        }else{
            //未勾选记住我
            //创建 仨cookie 分别存储 用户名 密码 勾选状态 存活时间为 0 给前端
            ck1.setMaxAge(0);
            ck2.setMaxAge(0);
            ck3.setMaxAge(0);
        }

        response.addCookie(ck1);
        response.addCookie(ck2);
        response.addCookie(ck3);

        return new ResponseEntity<>("ok",HttpStatus.OK );
    }


    @GetMapping("logOut2")
    public ResponseEntity<Void> logOut2(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        //清除 session
        session.removeAttribute("loginU");
        //清除 cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie ck : cookies) {
                ck.setMaxAge(0);
                ck.setPath("/");
                response.addCookie(ck);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK );

    }


}
