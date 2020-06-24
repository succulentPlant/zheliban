package com.zheliban.miaosha.controller;

import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public Result<Boolean> doLogin(LoginVo loginVo){
		log.info(loginVo.toString());
		//参数校验
		String mobile = loginVo.getMobile();
		String passInput = loginVo.getPassword();
		if(StringUtils.isEmpty(mobile)) {
			return Result.error(CodeMsg.MOBILE_EMPTY);
		}
		if(!ValidatorUtil.isMobile(mobile)) {
			return Result.error(CodeMsg.MOBILE_ERROR);
		}
		if(StringUtils.isEmpty(passInput)) {
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		}
		//登录
		CodeMsg cm = userService.login(loginVo);
		if(cm.getCode() == 0) {
			return  Result.success(true);
		}else {
			return Result.error(cm);
		}
	}
	
	

}
