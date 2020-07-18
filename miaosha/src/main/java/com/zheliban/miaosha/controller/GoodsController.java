package com.zheliban.miaosha.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.zheliban.miaosha.service.GoodsService;
import com.zheliban.miaosha.service.MiaoshaUserService;
import com.zheliban.miaosha.service.UserService;
import com.zheliban.miaosha.util.ValidatorUtil;
import com.zheliban.miaosha.vo.GoodsVo;
import com.zheliban.miaosha.vo.LoginVo;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService; 
	
	@Autowired
    GoodsService goodsService;
	
	@RequestMapping("/to_list")
	/*
	 * do_login的响应体设置了cookie，存到了浏览器里，to_list请求的时候带上了cookie
	 * 请求的时候会带上cookie，可以取值
	 * 有些手机客户端并不会把token放进cookie里传给我们的服务端，而是放到参数里面传递，为了兼容这种情况从Request里再取一下这个token
	 */
	public String toLogin(Model model , MiaoshaUser user){//实参来自于，WebConfig里的addArgumentResolvers
		
		model.addAttribute("user", user);//往前端传输用户信息
		//查询商品列表
		List<GoodsVo> list = goodsService.listGoodsVo();
		
        model.addAttribute("goodsList", list);//向前端传输商品列表
        return "goods_list"; //跳转到商品列表
        
		
	}
	@RequestMapping("/to_detail/{goodsId}")
    public String to_detail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {//
        model.addAttribute("user", user);
        
        GoodsVo goods =goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);
        
        long startAt = goods.getStartDate().getTime();//毫秒值
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        
        if (now < startAt){//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);//秒
        }else if(now>endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            miaoshaStatus = 1;
            remainSeconds =0;
        }
        
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        
        return "goods_detail"; //跳转到商品列表
    }
	
	
	

}
