package com.zheliban.miaosha.exception;

import com.zheliban.miaosha.result.CodeMsg;

/*
 * 定义一个全局异常
 */
public class GlobalException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;//？？？？
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm ) {
		super(cm.toString());
		this.cm = cm; 
	}

	public CodeMsg getCm() {
		return cm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	 

}
