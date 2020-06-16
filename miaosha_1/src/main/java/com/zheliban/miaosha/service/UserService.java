package com.zheliban.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zheliban.miaosha.dao.UserDao;
import com.zheliban.miaosha.domain.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public User getById(int id) {	//测试是否连接到数据库
		return userDao.getById(id);
	}
	
	//@Transactional					//事务的标签
	public boolean tx() {			//测试事务是否起作用
		User u1 = new User();
		u1.setId(2);
		u1.setName("张三");
		
		User u2 = new User();		//如果事务起作用，第二条数据插入异常，则因为事务回滚，第一条数据也不会被插入；否则，第一条数据没有异常将会被插入进表中
		u2.setId(1);
		u2.setName("李四");
		
		userDao.insert(u1);
		userDao.insert(u2);
		return true;
	}
}
