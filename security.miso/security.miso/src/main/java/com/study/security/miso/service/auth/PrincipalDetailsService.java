package com.study.security.miso.service.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.security.miso.domain.User.User;
import com.study.security.miso.domain.User.UserRepository;
import com.study.security.miso.web.dto.auth.SignupReqDto;

import lombok.RequiredArgsConstructor;
//spring Security에서 사용자 인증과 관련된 작업을 수행하기 위해 UserDetailsService를 구현하고,
//사용자 정보를 조회하고 저장하는 기능을 제공
//인중된 사용자의 정보를 객체에 담는다. 그 전의 단계에서..
//1번째
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = null;
		
		try {
			userEntity =  userRepository.findUSerByUserName(username);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException(username);
		}
		
		if(userEntity == null){
			throw new UsernameNotFoundException(username + "사용자이름을 사용할 수 없습니다.");
		}
		
	return new PrincipalDetails(userEntity);//어류가 없고 값이 있으면 객체를 생성해 리턴한다. 

//		if(!username.equals("test")){
//			throw new UsernameNotFoundException(username + "사용자이름을 사용할 수 없습니다.");
//		} 디비에서 판단해줄거임 
//		
//		UserDetails userDetails = new UserDetails() {
//			
//			@Override
//			public boolean isEnabled() {
//				return true;
//			}
//			
//			@Override
//			public boolean isCredentialsNonExpired() {
//				return true;
//			}
//			
//			@Override
//			public boolean isAccountNonLocked() {
//				return true;
//			}
//			
//			@Override
//			public boolean isAccountNonExpired() {
//				return true;
//			}
//			
//			@Override
//			public String getUsername() {
//				return "test";
//			}
//			
//			@Override
//			public String getPassword() {
//				return new BCryptPasswordEncoder().encode("1234");//복호화를 한것임 
//			}
//			
//			@Override
//			public Collection<? extends GrantedAuthority> getAuthorities() {
//				return null;
//			}
//		};
//		return userDetails;
	}
	
	public boolean addUser(SignupReqDto signupReqDto) throws Exception {
		return userRepository.save(signupReqDto.toEntity())>0;
	}
	
}
