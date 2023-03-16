package com.cloud.demo.service.impl;


import com.cloud.annotation.GPService;
import com.cloud.demo.service.IDemoService;

/**
 * 核心业务逻辑
 */
@GPService
public class DemoService implements IDemoService {

	public String get(String name) {
		return "My name is " + name + ",from service.";
	}

}
