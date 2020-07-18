package com.zheliban.miaosha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zheliban.miaosha.vo.GoodsVo;

@Mapper
public interface GoodsDao {
	//查找商品的所有项目+秒杀商品的库存、开始时间、截止时间、价格
	@Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price,g.goods_img from miaosha_goods mg left join goods g on mg.goods_id =g.id")
    public List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price,g.goods_img from miaosha_goods mg left join goods g on mg.goods_id =g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);
	
	

}
