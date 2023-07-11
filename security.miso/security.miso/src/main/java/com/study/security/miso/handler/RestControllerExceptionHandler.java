package com.study.security.miso.handler;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.study.security.miso.handler.exception.CustomVaildationApiException;
import com.study.security.miso.web.dto.CMResponseDto;

/*
 * CustomVaildationApiException이 발생하면 vaildationApiException 메서드가 호출되어 해당 예외를 처리하고,
 * ResponseEntity 객체를 반환
 * 이 예외 처리 과정에서 예외의 메시지와 오류 맵을 CMResponseDto 객체에 담아 클라이언트에게 응답
 */

@RestController
@ControllerAdvice //전역 예외 처리를 수행하는 클래스

public class RestControllerExceptionHandler {
	
	@ExceptionHandler(CustomVaildationApiException.class) //이 클래스타입이면 밑에서 낚아챔 
	public ResponseEntity<?> vaildationApiException(CustomVaildationApiException e) {
		return ResponseEntity.badRequest().body(new CMResponseDto<>(-1, e.getMessage(), e.getErrorMap()));
	}
}
