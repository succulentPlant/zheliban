package com.zheliban.miaosha.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zheliban.miaosha.dao.OrderDao;
import com.zheliban.miaosha.domain.MiaoshaOrder;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.domain.OrderInfo;
import com.zheliban.miaosha.vo.GoodsVo;
@Service
public class OrderService {
	
	@Autowired
    OrderDao orderDao;

	 public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId,Long goodsId){
	       return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
	 }

	
	@Transactional	//事务管理
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);
        
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        
        return orderInfo;
    }

	

}
