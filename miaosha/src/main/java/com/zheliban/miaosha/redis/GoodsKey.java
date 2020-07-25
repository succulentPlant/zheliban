package com.zheliban.miaosha.redis;

public class GoodsKey extends BasePrefix{

	public GoodsKey(int expireSecond, String prefix) {
		super(expireSecond, prefix);
		
	}
	public static GoodsKey getGoodsList = new GoodsKey(60,"gl");//商品列表在缓存中的有效期间为60毫秒，这样对用户的影响较小
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");

}
