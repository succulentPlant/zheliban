package com.zheliban.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	/*
	 * 将真实的密码加密成表单提交的密码----客户端
	 */
	private static final String salt = "1a2b3c4d";//客户端写死的一个salt
	
	public static String inputPassToFormPass(String inputPass) {//inputPass是用户实际输入的密码
		String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);//12inputPassc3 真实密码和salt拼接
		return md5(str);//加密
	}
	/*
	 * 将表单提交的密码再加密存到数据库----服务端
	 */
	public static String formPassToDBPass(String formPass , String salt) {//网络传输中的密码，随机的salt
		String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);//拼接
		return md5(str);//加密
	}
	/*
	 * 将客户端用户输入的明文密码两次加密转换成存入数据库的密码
	 */
	public static String inputPassToDBPass(String input ,String dbsalt) {
		String formPass = inputPassToFormPass(input);
		String dbPass = formPassToDBPass(formPass, dbsalt);
		return dbPass;
	}
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("123456"));//用户实际传输密码123456，加密以后在网络中的明文传输是d3b1294a61a07da9b49b6e22b2cbd7f9，即便这个串被不怀好意的人被截获并反查彩虹表，得到也只是121234563拼接后的密码，不是真正的密码
		//System.out.println(formPassToDBPass(inputPassToFormPass("123456") ,"1a2b3c4d"));
		System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
	}

}
