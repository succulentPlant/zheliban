package com.zheliban.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
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
	
	public static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;
	
	/*
	 * 根据id获取用户
	 */
	public  MiaoshaUser getById(long id){	//对象缓存
		//取缓存
        MiaoshaUser user = redisService.get(MiaoShaUserKey.getById,""+id,MiaoshaUser.class);
        if (user !=null){
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if (user !=null){
            redisService.set(MiaoShaUserKey.getById,""+id,user);//写进缓存
        }
        return user;
	}
	/*
	 * 根据token获取用户
	 */
	public MiaoshaUser getByToken(HttpServletResponse response,String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);	//对象缓存
		if (user!=null){
			//重新生成cookie ，以延长用户session的有效期
			addCookie(response,token,user);
		}
		return user;
	}
	/*
	 * 改密码
	 */
	public boolean updatePassword(String token,long id,String formPass){
        MiaoshaUser user = getById(id);
        if (user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoShaUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoShaUserKey.token,token,user);
        return true;
    }
	/*
	 * 根据登录信息进行判断，
	 * 如果出现错误就抛出异常，交给GlobalExceptionHander处理
	 * 否则，生成cookie写到response里去，返回true
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
		//生成通用唯一标识码
		String token = UUIDUtil.uuid();
		//生成cookie
		addCookie(response,token,user);
		return true;
	}
	/*
	 *生成新的cookie写到response——传递
	 */
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
		redisService.set(MiaoShaUserKey.token, token, user);//秒杀用户的键前缀+通用唯一识别码UUID(键) 用户信息id、name(值)，写入缓存redis
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);//生成一个cookie（name，vale）
		cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());//为了保持一致性，把cookie的有效期设置为和MiaoShaUserKey.toke键前缀的有效期
		cookie.setPath("/");//设置为网站的根目录
		response.addCookie(cookie);//将cookie写到响应，继而传递到客户端
	}

}
