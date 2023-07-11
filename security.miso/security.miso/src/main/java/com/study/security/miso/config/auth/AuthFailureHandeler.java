package com.study.security.miso.config.auth;

import java.io.IOException;
import java.lang.reflect.Executable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
// Spring Security에서 인증 실패 시 실행되는 핸들러
public class AuthFailureHandeler implements AuthenticationFailureHandler{
	
	@Override //예외가 발생하면 다 여기서 해결
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		System.out.println(exception.getMessage());
		response.setContentType("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print("<html><head></head><body><script>alert(\"로그인실패\");history.back();</script></body></html>");
		
	}

}
