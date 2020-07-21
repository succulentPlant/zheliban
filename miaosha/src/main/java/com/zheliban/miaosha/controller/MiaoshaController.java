package com.zheliban.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zheliban.miaosha.domain.MiaoshaOrder;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.domain.OrderInfo;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.service.GoodsService;
import com.zheliban.miaosha.service.MiaoshaService;
import com.zheliban.miaosha.service.MiaoshaUserService;
import com.zheliban.miaosha.service.OrderService;
import com.zheliban.miaosha.vo.GoodsVo;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
	
	@Autowired
    MiaoshaUserService userService;
	
    @Autowired
    RedisService redisService;
    
    @Autowired
    GoodsService goodsService;
    
    @Autowired
    OrderService orderService;
    
    @Autowired
    MiaoshaService miaoshaService;
    

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {//
        model.addAttribute("user", user);//往前端传输用户信息
        //如果用户没有登录，就返回登录页面
        if (user == null){
            return "login";
        }
        
        //判断库存,如果库存小于等于0，就返回秒杀失败
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());//
            return "miaosha_fail";
        }
        
        //判断是否已经秒杀到了，如果已经秒杀到了，也返回呢秒杀失败
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order != null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getCode());
            return "miaosha_fail";
        }
        
        //秒杀：减库存、下订单、写入秒杀订单(原子性，用事务去保证3个动作都被执行或者都不成功)
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        //将订单和商品信息传到前端
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);

        return "order_detail"; //跳转到商品列表
        
    }
}
