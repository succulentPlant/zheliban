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
import com.zheliban.miaosha.domain.MiaoshaUser;
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
@RequestMapping("/goods")
public class GoodsController {
	
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/to_list")
	public String toLogin(Model model){
		model.addAttribute("user", new MiaoshaUser());
		return "goods_list"; 
	}
	

	

}
