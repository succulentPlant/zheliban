package com.zheliban.miaosha.result;

public class Result<T> {

	private int code;
	private String msg;
	private T data;
	
	private Result(T data) {		//成功时候调用的构造函数
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	private Result(CodeMsg cm) {	//失败的时候调用的构造函数
		if(cm == null) {
			return;
		}
		this.code = cm.getCode();
		this.msg = cm.getMsg();
	}
	//成功时候的调用
	public static <T> Result<T> success(T data){
		return new Result<T>(data);
	}
	//失败时候的调用
	public static <T> Result<T> error(CodeMsg cm){
		return new Result<T>(cm);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	

}
