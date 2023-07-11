package com.study.security.miso.handler.aop;

import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.Joinable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.study.security.miso.handler.exception.CustomVaildationApiException;

@Aspect
@Component
public class ValidationAop {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Pointcut("@annotation(com.study.security.miso.handler.aop.annotation.ValidCheck)")
	private void enableVaild() {};
	
	@Before("enableVaild()")
	public void ValidBefore(JoinPoint joinPoint) {
		
		Object[] args = joinPoint.getArgs(); //매개변수를 넣음
		LOGGER.info(">>>> 유효성 검사중....<<<<");
		
		for(Object arg: args) {
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMessage = new HashMap<>();
					bindingResult.getFieldErrors().forEach(error -> {
						errorMessage.put(error.getField(), error.getDefaultMessage());
						System.out.println("오류발생 필드명:" + error.getField());
						System.out.println("오류발생 상세메시지: " + error.getDefaultMessage());
					});
					throw new CustomVaildationApiException("유효성 검사 실패", errorMessage); //중복코드를 없애기 위해aop를 사용
				}
			}
		}
	}
	@AfterReturning(value = "enableVaild()", returning = "returnObj")
	public void afterRetrun(JoinPoint joinPoint, Object returnObj) {
		LOGGER.info("유효성 검사 완료: {}", returnObj);
	}
	
}
