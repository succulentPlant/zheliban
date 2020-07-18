package com.zheliban.miaosha.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.zheliban.miaosha.domain.User;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.redis.UserKey;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.result.Result;
import com.zheliban.miaosha.service.MiaoshaUserService;
import com.zheliban.miaosha.service.UserService;
import com.zheliban.miaosha.util.ValidatorUtil;
import com.zheliban.miaosha.vo.LoginVo;


@Controller
@RequestMapping("/login")
public class LoginController {
	
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	//controller层的两种功能：1、rest api json输出 		 2、页面
	
	@RequestMapping("/to_login")
	public String toLogin(){
		return "login"; 
	}
	
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(HttpServletResponse response , @Valid LoginVo loginVo){	//@Valid
		log.info(loginVo.toString());//根据指定的格式和参数在INFO级别记录一条消息
		userService.login(response,loginVo);//登录MiaoshaUserService，如果出现各种各样的异常GlobalException就向外抛，GlobalExceptionHande拦截异常将异常输出
		return Result.success(true);
	}
	
	

}
