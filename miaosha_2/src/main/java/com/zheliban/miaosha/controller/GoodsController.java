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
import org.springframework.web.bind.annotation.CookieValue;
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
	/*
	 * do_login的响应体设置了cookie，存到了浏览器里，to_list请求的时候带上了cookie
	 * 请求的时候会带上cookie，可以取值
	 * 有些手机客户端并不会把token放进cookie里传给我们的服务端，而是放到参数里面传递，为了兼容这种情况从Request里再取一下这个cookie
	 */
	public String toLogin(Model model , 
			@CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN , required = false) String cookieToken ,
			@RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN , required = false) String paramToken){//
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return "login";		//如果两个都是空就返回登录页面
		}
		//定义优先级
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		//拿到token之后就可以获得用户的信息了
		MiaoshaUser user = userService.getByToken(token);
		model.addAttribute("user", user);
		return "goods_list"; //跳转到商品列表
	}
	

	

}
