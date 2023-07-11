package com.study.security.miso.web.controller.api;

import java.awt.color.CMMException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.security.miso.handler.aop.annotation.Log;
import com.study.security.miso.handler.aop.annotation.Timer;
import com.study.security.miso.handler.aop.annotation.ValidCheck;
import com.study.security.miso.handler.exception.CustomVaildationApiException;
import com.study.security.miso.service.auth.AuthService;
import com.study.security.miso.service.auth.PrincipalDetails;
import com.study.security.miso.service.auth.PrincipalDetailsService;
import com.study.security.miso.web.dto.CMResponseDto;
import com.study.security.miso.web.dto.auth.SignupReqDto;
import com.study.security.miso.web.dto.auth.UserNameCheckReqDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final PrincipalDetailsService principalDetailsService;
	private final AuthService authService;
	
	
	@ValidCheck
	@PostMapping("/signup") 
	public ResponseEntity<?> signup(@RequestBody @Valid SignupReqDto signupReqDto, BindingResult bindingResult) {

		boolean status = false;
		
		try {
			status = principalDetailsService.addUser(signupReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new CMResponseDto<>(-1,"회원가입 실패",status));
		}
		return ResponseEntity.ok().body(new CMResponseDto<>(1,"회원가입 성공",status));
	}
	
	@ValidCheck
	@Log
	@Timer
	@GetMapping("/signup/validation/username") //BindingResult bindingResult:@Valid 쓰면 따
	public ResponseEntity<?> checkUsername(@Valid UserNameCheckReqDto userNameCheckReqDto, BindingResult bindingResult) {	
		
		boolean status = false;
		
		try {
			status = authService.checkUsername(userNameCheckReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new CMResponseDto<>(-1, "서버오류", status));

		}
		return ResponseEntity.ok().body(new CMResponseDto<>(1, "회원가입 가능 여부", status));
	}
		
	@GetMapping("/principal")
	public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(principalDetails == null) {
			return ResponseEntity.badRequest().body(new CMResponseDto<>(-1, "principal is null", null));
		}
		return ResponseEntity.ok(new CMResponseDto<>(1, "success load", principalDetails.getUser()));

	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

