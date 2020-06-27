package com.zheliban.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zheliban.miaosha.dao.MiaoshaUserDao;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.exception.GlobalException;
import com.zheliban.miaosha.redis.MiaoShaUserKey;
import com.zheliban.miaosha.redis.RedisService;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.util.MD5Util;
import com.zheliban.miaosha.util.UUIDUtil;
import com.zheliban.miaosha.vo.LoginVo;
/*
 * 业务
 */
@Service
public class MiaoshaUserService {
	
	private static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;
	
	public  MiaoshaUser getById(long id){
		return miaoshaUserDao.getById(id);
	}
	/*
	 * 根据登录信息进行判断，
	 * 如果出现错误就抛出异常，交给GlobalExceptionHander处理
	 * 否则，返回true
	 * 
	 */
	public boolean login(HttpServletResponse response ,  LoginVo loginVo) {		//返回真正代表业务逻辑的东西———登录（成功 or 失败）———boolean
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String dbSalt = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie,写到response里去
		String token = UUIDUtil.uuid();
		redisService.set(MiaoShaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());//设置cookie的有效期为MiaoShaUserKey.toke键前缀的有效期
		cookie.setPath("/");//设置为网站的根目录
		response.addCookie(cookie);//将cookie写到客户端里面去
		return true; 
		
		
	}

}
