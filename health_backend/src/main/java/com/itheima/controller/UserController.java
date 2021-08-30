package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("loginSuccess")
    public Result loginSuccess() {
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @RequestMapping("loginFail")
    public Result loginFail() {
        return new Result(false, MessageConstant.LOGIN_FAIL);
    }

    @RequestMapping("/getUsername")
    public Result getUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            final String username = user.getUsername();
            return new Result(true, MessageConstant.LOGIN_SUCCESS, username);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
    }

}
