package com.zheliban.miaosha.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.result.Result;

/*
 * 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHander {
	
	@ExceptionHandler(value = Exception.class)//	所有的异常都需要拦截
	
	public Result<String> exceptionHander(HttpServletRequest request , Exception e){
		if(e instanceof GlobalException) {//如果是全局异常
			GlobalException ex = (GlobalException)e;
			return Result.error(ex.getCm());
		}else if(e instanceof BindException) {	//如果是绑定异常
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();//异常可能不只有一个
			ObjectError error = errors.get(0);//获取第一个error
			String msg = error.getDefaultMessage();//获取这个error的默认信息
			return Result.error( CodeMsg.BIND_ERROR.fillArgs(msg) ); 
		}else {		//如果不是，默认为服务端异常
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}

}
