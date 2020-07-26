package com.zheliban.miaosha.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zheliban.miaosha.dao.OrderDao;
import com.zheliban.miaosha.domain.MiaoshaOrder;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.domain.OrderInfo;
import com.zheliban.miaosha.redis.OrderKey;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.vo.GoodsVo;
@Service
public class OrderService {
	
	@Autowired
    OrderDao orderDao;
	
	@Autowired
	RedisService redisService;
	
	 public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId,Long goodsId){
//	       return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
		 return redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + '_' + userId , MiaoshaOrder.class);
	 }

	 public OrderInfo getOrderById(long orderId) {
		 return orderDao.getOrderById( orderId);
	 }
	
	@Transactional	//事务管理
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();					//生成订单
        orderInfo.setCreateDate(new Date());					//设置订单细节
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);				//将订单插入数据库，返回订单Id
        
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();			//生成秒杀订单
        miaoshaOrder.setGoodsId(goods.getId());					//设置秒杀订单的细节
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);				//将秒杀订单插入数据库，返回秒杀订单的详情
        
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + '_' + goods.getId() , miaoshaOrder);//写进缓存
        return orderInfo;
    }



	

}
