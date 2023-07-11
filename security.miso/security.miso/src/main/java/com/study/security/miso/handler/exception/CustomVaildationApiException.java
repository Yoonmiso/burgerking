package com.study.security.miso.handler.exception;
/*
 * CustomVaildationApiException은 사용자 정의 예외 클래스입니다.
 * 이 클래스는 RuntimeException을 상속받아 예외를 처리하는데 사용됩니다.
 * 주로 유효성 검사 실패나 API 요청 처리 중 발생한 예외 상황을 나타내는데 사용될 수 있습니다.
 */

//예외 발생 시 메시지와 오류 맵(errorMap)을 저장
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
@Getter
public class CustomVaildationApiException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errorMap;

	public CustomVaildationApiException() {
		this("error", new HashMap<String, String>()); //error은 message로 간다.
	}
	
	public CustomVaildationApiException(String message) {
		this(message, new HashMap<String, String>());
	}
	
	public CustomVaildationApiException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
	
}
