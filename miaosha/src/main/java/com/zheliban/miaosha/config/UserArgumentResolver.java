package com.zheliban.miaosha.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.druid.util.StringUtils;
import com.zheliban.miaosha.service.MiaoshaUserService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private MiaoshaUserService miaoshaUserService;
	public boolean supportsParameter(MethodParameter parameter) {
		
		return false;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		 HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		 HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		 
	     String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
	     String cookieToken = getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
	     
	    if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
	        return null;//如果两个都是空就返回登录页面
	    }
	   
	    String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;//定义优先级，先从参数里取token，如果参数为空，再从cookie里取
	    
	    return miaoshaUserService.getByToken(response,token);//拿到token之后就可以从redis里获得用户的信息了
	}

	private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies=request.getCookies();
        if(cookies==null||cookies.length<=0){
            return null;
        }
        for (Cookie cookie:cookies) {//遍历所有cookie找到我们想要的
            if (cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
	}	
	

}
