package com.zheliban.miaosha.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
@Service
public class RedisService {
	
	@Autowired
	JedisPool jedisPool;
	
	public <T> T get(String key , Class<T> clazz) {
		Jedis jedis = jedisPool.getResource();
	}
	
	@Bean
	public JedisPool JedisPoolFactory() {
		
	}
}
