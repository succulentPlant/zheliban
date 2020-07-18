package com.zheliban.miaosha.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
    UserArgumentResolver userArgumentResolver;
	
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){//框架会回调这个方法给controller里面方法的参数赋值
        resolvers.add(userArgumentResolver);
    }
	
		
 
}
