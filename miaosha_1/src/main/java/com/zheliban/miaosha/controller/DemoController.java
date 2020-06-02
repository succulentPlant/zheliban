package com.zheliban.miaosha.controller;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zheliban.miaosha.result.CodeMsg;
import com.zheliban.miaosha.result.Result;


@RestController
@RequestMapping("/demo")
public class DemoController {
	
	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "hello aa";
	}
	//controller层的两种功能：1、rest api json输出 		 2、页面
	@RequestMapping("/hello")
	@ResponseBody
	public Result<String> hello(){
		return Result.success("hello.word");
	}
	@RequestMapping("/helloError")
	@ResponseBody
	public Result<String> helloError(){
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	@RequestMapping("/thymeleaf")
	public String  thymeleaf(Model model){
		model.addAttribute("name", "三毛姑卡");
		return "hello";
	}

}
