package com.study.security.miso.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.study.security.miso.service.auth.PrincipalDetails;

//@AuthenticationPrincipal는 principalDetails객체를 받기 위해 rest, 그냥컨드롤러 모두사용가능
//컨토롤러에서만 model 사용가능한거 thml에 model을 던져주는것 위에 타임리프 달아줘서 가능한것 

@Controller
public class PageController {
	
	@GetMapping({"/", "/index"})
	public String loadIndex(Model model , @AuthenticationPrincipal PrincipalDetails principalDetails) {
		model.addAttribute("principal", principalDetails);
		return"index";
	}
	
	@GetMapping("/auth/signin")
	public String loadSignin() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String loadString() {
		return "auth/signup";
	}
	
	@GetMapping("/mypage")
	public String loadMypage() {
		return "mypage";
	}
}
