package com.zheliban.miaosha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;


public class ValidatorUtil {	//作为一个工具类，简单校验了是否为手机号
	
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");//将给定的正则表达式编译为带有给定标志的模式，Pattern:模式类，正则表达式的编译形式。
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {//手机号为空就返回false
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);//返回此模式的匹配标志。
		return m.matches();//对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值
		
	}
	
//	public static void main(String[] args) {
//		System.out.println(isMobile("19811122233"));
//		System.out.println(isMobile("198111222"));
//	}
}
