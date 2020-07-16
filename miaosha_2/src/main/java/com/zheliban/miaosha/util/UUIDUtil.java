package com.zheliban.miaosha.util;

import java.util.UUID;
/*
 * UUID
 * 含义：通用唯一识别码
 * 目的：让分布式系统中的所有元素，都能有唯一的辨识资讯
 */
public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");//jdk自带的，原生UUID随机生成一个tooken
	}

}
