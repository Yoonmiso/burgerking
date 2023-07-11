package com.project.burgerking.web.controller;
//없어져야 풀 완료

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
	@GetMapping("")
	public String Test() {
		return "testtt";
	}
}
