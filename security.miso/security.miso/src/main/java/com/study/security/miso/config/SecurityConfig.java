package com.study.security.miso.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.study.security.miso.config.auth.AuthFailureHandeler;
import com.study.security.miso.service.auth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity //상속을 비활성화 하고, 우리가 만든거 쓰기위함 , Spring Security를 활성화
@Configuration //ioc 등록 
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;
	
	private final PrincipalOauth2UserService oauth2UserService;
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoding() {
		return new BCryptPasswordEncoder();
	}//암호화를 해줌 관리자도 정보를 볼 수 업슴 
	
	
	// HTTP 요청에 대한 보안 설정을 구성, 메서드를 재정의하여 웹 애플리케이션의 보안 규칙을 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); //회원가입 정보를 관리자도 몰라야한다.
							   //우리쪽에 올때 낚아채는 것들을 막고자 하는 것이다. (csrf 공격)
		
		http.headers()
			.frameOptions()
			.disable();
		http.addFilter(corsFilter);
		
		http.authorizeRequests()	//post controller 안만들어도 됨 애가 해줘서 
			.antMatchers("/api/v1/grant/test/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")	
			
			.antMatchers("/api/v1/grant/test/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")	
			
			.antMatchers("/api/v1/grant/test/admin/**")
			.access("hasRole('ROLE_ADMIN')")	
			
			.antMatchers("/notice/addition", "/notice/modification/**")
			.hasRole("ADMIN")
			
		    
			.antMatchers("/","/index","mypage/**")			//1) 우리가 지정한 요청  mypage/어떤것을 해도 6번 페이지 연다
			.authenticated()								//2) 인증을 거처라 
			.anyRequest()									//3) 다른 모든 요청들은
			.permitAll()									//4) 모두 접근 권한을 부여하겠다.
			.and()								
			.formLogin()									//5)로그인 방식은 form 형식을 사용하겠다.
			.loginPage("/auth/signin")						//6)로그인 페이지는 해당 get요청을 통해 접근 
			.loginProcessingUrl("/auth/signin")				//7)로그인 요청(post요청)
			.failureHandler(new AuthFailureHandeler()) //예외가 생기면 우리가 만든걸로 가는 거임 
			
			.and()
			.oauth2Login()
			.userInfoEndpoint()
			.userService(oauth2UserService)
			.and()
			.defaultSuccessUrl("/index");
		
	        /*
	         * 순서
	         * 1 -> 2 -> 6 -> 5 -> 7 ->
	         */
	}
}
