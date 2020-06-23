package com.zheliban.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zheliban.miaosha.domain.User;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.redis.UserKey;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.result.Result;
import com.zheliban.miaosha.service.UserService;


@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "hello aa";
	}
	//controller层的两种功能：1、rest api json输出 		 2、页面
	
	@RequestMapping("/to_login")
	@ResponseBody
	public String toLogin(){
		return "login"; 
	}
	
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(){
		return null;
	}
	
	

}
