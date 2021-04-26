package com.redis_study.controller;


import com.redis_study.model.Users;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("用戶管理")
@RequestMapping("/api/")
public class UsersController {

    @Autowired
    private UserService userService;


    @ApiOperation("通過id獲得用戶數據")
    @GetMapping("/getUserById")
    public ReturnMessageModel<Users> getUserById(String id){

        return new ReturnMessageModel<Users>( userService.getUserById(id));
    }



    @ApiOperation("初始化用戶數據")
    @GetMapping("/initUsers")
    public void  initUsers(){

        userService.initUser();
    }
}
