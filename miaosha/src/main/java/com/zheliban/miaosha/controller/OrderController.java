package com.zheliban.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.domain.OrderInfo;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.result.Result;
import com.zheliban.miaosha.service.GoodsService;
import com.zheliban.miaosha.service.MiaoshaUserService;
import com.zheliban.miaosha.service.OrderService;
import com.zheliban.miaosha.vo.GoodsVo;
import com.zheliban.miaosha.vo.OrderDetailVo;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user, @RequestParam("orderId") long orderId) {
        model.addAttribute("user", user);
        if (user==null){
            return Result.error(CodeMsg.SESION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goodsVo);
        vo.setOrder(orderInfo);
        return Result.success(vo);//返回订单详情
    }

}