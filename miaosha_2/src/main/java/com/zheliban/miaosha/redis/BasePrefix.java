package com.zheliban.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix {
	
	private int expireSecond;	//过期时间
	private String prefix;		//key的前缀
	
	public BasePrefix(String prefix) {	//默认 expireSecond=0 代表永不过期
		this(0,prefix);
	}
	public BasePrefix(int expireSecond,String prefix) {
		this.expireSecond = expireSecond;
		this.prefix = prefix;
	}

	public int expireSeconds() {	
		
		return expireSecond;
	}

	public String getPrefix() {
		String className = getClass().getSimpleName();//获取类名
		return className + ":" + prefix;		//区分来自不同板块的key
	}

}
  