package com.zheliban.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zheliban.miaosha.dao.UserDao;
import com.zheliban.miaosha.domain.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public User getById(int id) {
		return userDao.getById(id);
	}
}
