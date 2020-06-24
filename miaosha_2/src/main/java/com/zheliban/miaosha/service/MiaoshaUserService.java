package com.zheliban.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zheliban.miaosha.dao.MiaoshaUserDao;
import com.zheliban.miaosha.domain.MiaoshaUser;
import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.util.MD5Util;
import com.zheliban.miaosha.vo.LoginVo;

@Service
public class MiaoshaUserService {
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	public  MiaoshaUser getById(long id){
		return miaoshaUserDao.getById(id);
	}

	public CodeMsg login(LoginVo loginVo) {
		if(loginVo == null) {
			return CodeMsg.SERVER_ERROR;
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			return CodeMsg.MOBILE_NOT_EXIST;
		}
		//验证密码
		String dbPass = user.getPassword();
		String dbSalt = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
		if(!calcPass.equals(dbPass)) {
			return CodeMsg.PASSWORD_ERROR;
		}
		return CodeMsg.SUCCESS; 
		
		
	}

}
