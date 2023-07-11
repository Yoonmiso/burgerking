package com.study.security.miso.handler.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) //언제,,, 적용시킬거냐 런타임중에 실행 
@Target({ TYPE, METHOD }) //어디,,,에 적용시킬거냐 
public @interface Timer {
	
}
