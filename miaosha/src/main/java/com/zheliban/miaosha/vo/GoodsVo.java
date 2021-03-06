package com.zheliban.miaosha.vo;

import java.util.Date;

import com.zheliban.miaosha.domain.Goods;
/*
 * 
 */
public class GoodsVo extends Goods {//将商品表和秒杀商品表整合到一块
	
	private Double miaoshaPrice;
	private Integer stockCount;
    private Date startDate;
    private Date endDate;
    
    public Double getMiaoshaPrice() {
    	return miaoshaPrice;
    }
    public void setMiaoshaPrice(Double miaoshaPrice) {
    	this.miaoshaPrice = miaoshaPrice;
    }
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
    

}
