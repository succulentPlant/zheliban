package com.zheliban.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {
	
	@Autowired
	RedisConfig redisConfig;
	
	@Bean	//告诉方法，产生一个bean，并交给Spring容器
	public JedisPool JedisPoolFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();//创建连接池的配置对像，对连接池进行相关参数的配置
		poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
		JedisPool jedisPool = new JedisPool();//创建连接池对像
// 		JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout()*1000, redisConfig.getPassword(), 0);
		JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout()*1000);
		return jp;
	}
}
