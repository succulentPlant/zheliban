package com.zheliban.miaosha.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.domain.OrderInfo;
import com.zheliban.miaosha.vo.GoodsVo;
@Service
public class MiaoshaService {
	/*
	 * 自己的service只操作自己的Dao
	 * 操作其他Dao，通过其他的service
	 */
	@Autowired
    GoodsService goodsService;
	
    @Autowired
    OrderService orderService;

    @Transactional //注解事务管理，如果出现异常，就回滚（因为减库存、生成订单这两个操作必须一起成功要么一起失败）
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 
        goodsService.reduceStock(goods);
        //生成订单
        return orderService.createOrder(user,goods);
    }
	
}
