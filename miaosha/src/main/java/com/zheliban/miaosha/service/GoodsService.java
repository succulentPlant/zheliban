package com.zheliban.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zheliban.miaosha.dao.GoodsDao;
import com.zheliban.miaosha.domain.MiaoshaGoods;
import com.zheliban.miaosha.vo.GoodsVo;


/*
 * 业务
 */
@Service
public class GoodsService {
	@Autowired
	GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){ 
        return goodsDao.listGoodsVo();//返回秒杀商品列表
    }

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		
		return goodsDao.getGoodsVoByGoodsId(goodsId);//返回秒杀商品详情
	}

	public void reduceStock(GoodsVo goods) {//减少库存
		MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        goodsDao.reduceStock(g);
	}
}
