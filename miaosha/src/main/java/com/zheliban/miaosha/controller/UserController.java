package com.zheliban.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.result.Result;
import com.zheliban.miaosha.service.MiaoshaUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    MiaoshaUserService userService;
    
    @Autowired
    RedisService redisService;


    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> to_list(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        return Result.success(user);
    }

}
