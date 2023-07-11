package com.project.burgerking.web.controller;
//gitTest

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
	@GetMapping("")
	public String Test() {
		return "testtt";
	}
}
