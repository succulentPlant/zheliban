package com.zheliban.miaosha.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;
@Service
public class RedisService {		//希望通过service来提供redis的服务
	
	@Autowired		//让Spring完成Bean的自动装配工作
	JedisPool jedisPool;
	
	@Autowired
	RedisConfig redisConfig;
	public <T> T get(String key , Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedisPool.getResource();
			String str = jedis.get(key);
			T t = stringToBean(str,clazz);
			return t; 
		} finally {
			returnToPool(jedis);
		}
	}
	public <T> boolean set(String key ,T value ) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str = beanToString(value);
			if(str == null ||str.length() <= 0) {
				return false;
			}
			jedis.set(key, str);
			return true;  
		} finally {
			returnToPool(jedis);
		}
	}
	/*
	 * 用Fastjson把bean对象转换称字符串写到redis里面去
	 * 因此，读出来的时候要把字符串转换bean对象
	 */
	private <T> String beanToString(T value) {
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass(); 
		if(clazz == int.class || clazz == Integer.class) {
			return "" + value;
		}else if(clazz == String.class) {
			return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return "" + value; 
		}else {
			return JSON.toJSONString(value); 
		}	
	}
	@SuppressWarnings("unchecked")
	private <T> T stringToBean(String str,Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return (T)Long.valueOf(str); 
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz); 
		}
	}

	private void returnToPool(Jedis jedis) {
		if(jedis != null) {
			jedis.close();
		}
	}
}
 