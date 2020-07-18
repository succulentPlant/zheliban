package com.zheliban.miaosha.redis;

public class MiaoShaUserKey	extends BasePrefix {
	
	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;//默认过期时间3600秒*24小时*2天
	
	public MiaoShaUserKey(int expireSeconds , String prefix) {
		super(expireSeconds , prefix);
	}
	
	public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE , "tk");
	
}
