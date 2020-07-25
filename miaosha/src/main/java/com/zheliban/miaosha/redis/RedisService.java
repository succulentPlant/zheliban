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
	/*
	 * 获取对象
	 */
	public <T> T get(KeyPrefix prefix , String key , Class<T> clazz) {//读，键前缀，键，T类类型的类对象
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接key的各个部分成完整的一个键，真正的键
			String str = jedis.get(realKey);//根据键获取值
			T t = stringToBean(str,clazz);//将字符串类型的值转换成T类型对象
			return t; 
		} finally {
			returnToPool(jedis);
		}
	}
	/*
	 * 写对象
	 */
	public <T> boolean set(KeyPrefix prefix , String key ,T value ) {//写，键的前缀，键，值
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str = beanToString(value);//将T类型的值转换成字符串
			if(str == null ||str.length() <= 0) {
				return false;
			}
			String realKey = prefix.getPrefix()+key;//拼接真正的键
			int seconds = prefix.expireSeconds();
			if(seconds <= 0) {	//过期时间小于等于0，默认永不过期
				jedis.set(realKey, str);//将键值对写进去
			}else {
				jedis.setex(realKey, seconds, str);//将键值对和过期时间都写进去
			}
			
			return true;  
		} finally {
			returnToPool(jedis);
		}
	}
	/*
	 * 判断对象是否存在
	 */
	public <T> boolean exist(KeyPrefix prefix , String key) {//判断键是否存在
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接真正的键
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	 /*
     * 删除对象
     */
    public boolean delete(KeyPrefix prefix, String key) {//判断键是否存在
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;//拼接真正的键
            return jedis.del(key)>0;
        } finally {
            returnToPool(jedis);
        }
    }
	/*
	 * 增加值
	 */
	public <T> Long incr(KeyPrefix prefix , String key) {//
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接真正的键
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	/*
	 * 减少值(值-1，如果键不存在或者错误类型，则设置为0再-1)
	 */
	public <T> Long decr(KeyPrefix prefix , String key) {//
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接真正的键
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	/*
	 * 用Fastjson把bean对象转换称字符串写到redis里面去
	 * 因此，读出来的时候要把字符串转换bean对象
	 */
	@SuppressWarnings("unchecked")
	private <T> T stringToBean(String str,Class<T> clazz) {	//把String类型的值转换成T类型的值
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
			return JSON.toJavaObject(JSON.parseObject(str), clazz); //将JSON字符串转换成相应的对象
		}
	}
	
	private <T> String beanToString(T value) {	//把bean对象转换成String对象
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();						//获取这个value这个类对象
		if(clazz == int.class || clazz == Integer.class) {		//判断是不是整型对象
			return "" + value;									//如果是，整型对象转换成字符串对象
		}else if(clazz == String.class) {						//判断是不是字符串对象
			return (String)value;								//如果是，直接返回对象
		}else if(clazz == long.class || clazz == Long.class) {	//判断是不是长整型对象
			return "" + value; 									//如果是，长整型对象转换成字符串对象
		}else {
			return JSON.toJSONString(value); 					//将实体对象转换成JSON字符串
		}	
	}

	private void returnToPool(Jedis jedis) {
		if(jedis != null) {
			jedis.close();
		}
	}
	public void delete(MiaoShaUserKey getById, String string) {
		
	}
}
 