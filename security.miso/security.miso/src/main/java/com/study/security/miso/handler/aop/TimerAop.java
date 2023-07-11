package com.study.security.miso.handler.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
//왕중요 외우지마세요 

//aop는 중복코드를 제거하기 위함 
@Aspect
@Component
public class TimerAop {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass()); //getClass()는 TimerAop을뜻함
	
	//모든 컨트롤러 클래스의 모든 메서드를 대상으로 포인트컷을 설정
	@Pointcut("execution(* com.study.security.miso.web.controller..*.*(..))")
	private void pointCut() {};
	
	//Timer 어노테이션이 적용된 메서드를 대상으로 포인트컷을 설정
	@Pointcut("@annotation(com.study.security.miso.handler.aop.annotation.Timer)")
	private void enableTimer() {};
	
	
	
	@Around("pointCut() && enableTimer()")//method 실행 전후 가능 
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		
		Object result = joinPoint.proceed(); // 동작 전후의 기준 
		
		stopwatch.stop();
		
		LOGGER.info("메소드 실행시간: {}({}) = {} ({}ms)",
				joinPoint.getSignature().getDeclaringType(),
				joinPoint.getSignature().getName(),
				result,
				stopwatch.getTotalTimeSeconds());
		return result;
	}
}
