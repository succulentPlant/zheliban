package com.zheliban.miaosha.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import com.alibaba.druid.util.StringUtils;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.redis.GoodsKey;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.result.Result;
import com.zheliban.miaosha.service.GoodsService;
import com.zheliban.miaosha.service.MiaoshaUserService;
import com.zheliban.miaosha.vo.GoodsDetailVo;
import com.zheliban.miaosha.vo.GoodsVo;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService; 
	
	@Autowired
    GoodsService goodsService;
	
	@Autowired
    ThymeleafViewResolver thymeleafViewResolver;
	 
	/*
	 * 页面缓存
	 */
	@RequestMapping(value = "/to_list",produces = "text/html")	//一个用来处理请求地址映射的注解，字段value指定请求的实际地址，字段produces指定返回的内容类型
    @ResponseBody//将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据
	public String toLogin(Model model , MiaoshaUser user , HttpServletRequest request, HttpServletResponse response){//实参来自于，WebConfig里的addArgumentResolvers		
		
		model.addAttribute("user", user);//往前端传输用户信息，前端通过el表达式${}可以获取的到
		
		List<GoodsVo> list = goodsService.listGoodsVo();//查询商品列表
        model.addAttribute("goodsList", list);//向前端传输商品列表

        
        //先从缓存里面取，如果缓存里存在该页面就返回该html页面		无参数缓存	页面缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);//取
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //如果缓存里没有，就手动渲染页面
        IWebContext ctx =new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);	//参数：模版 
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);//写
        }
        
        return html; //跳转到商品列表
        
//        return "goods_list"; //跳转到商品列表	SringBoots渲染的
	}
	/*
	 * URL缓存(演示一下而已)
	 */
	@RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")//商品不同跳转的详情页也不同（携带变量goodsId）
    @ResponseBody
	public String to_detail2(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId, HttpServletRequest request, HttpServletResponse response) {//
        model.addAttribute("user", user);
        
        //先从缓存里面取，如果缓存里存在该页面就返回该html页面		有参数的缓存		URL缓存
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);//根据键获取值
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        
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
        
        //如果取不到，就手动渲染页面
        IWebContext ctx =new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);//写
        }
     
        return html;
        
//        return "goods_detail"; //跳转到商品列表
        
    }
	/*
	 * 页面静态化（主要用的）
	 */
	@RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> to_detail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId, HttpServletRequest request, HttpServletResponse response) {//
        
		GoodsVo goods =goodsService.getGoodsVoByGoodsId(goodsId);
		
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt){//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        }else if(now>endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            miaoshaStatus = 1;
            remainSeconds =0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(user);
      
        return Result.success(vo);	//返回订单详情GoodsDetailVo
    }
	
	
	
	

}
