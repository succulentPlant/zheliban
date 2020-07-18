package com.zheliban.miaosha.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alibaba.druid.util.StringUtils;
import com.zheliban.miaosha.util.ValidatorUtil;
/*
 * 定义一个手机号校验器
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String>{//<注解，注解的参数的类型>
	
	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {//初始化
		required = constraintAnnotation.required();//required()，默认为true————必须有值
	}
	
	public boolean isValid(String value, ConstraintValidatorContext context) {//值为手机号
		if(required) {	//如果值是必须的
			return ValidatorUtil.isMobile(value);	//判断值的格式————手机号是否合法
		}else {			//如果值不是必须的
			if(StringUtils.isEmpty(value)) {	//值为空，则返回true
				return true;
			}else {								//值不为空，判断值的格式————手机号是否合法
				return ValidatorUtil.isMobile(value);
			}
		}
		
	}
	

}
