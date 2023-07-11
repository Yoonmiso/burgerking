package com.study.security.miso.service.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.study.security.miso.domain.User.User;

import lombok.Data;
//Spring Security에서 인증된 사용자의 정보를 담는 역할을 수행
//2번째, 객체로 이용을 함 
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private Map<String, Object> attribute;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user, Map<String, Object> attribute) {
		this.user = user;
		this.attribute = attribute;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		
		List<String> roleList = user.getUserRoles();
		
		roleList.forEach(role -> {
			grantedAuthorities.add(() -> role);
		});
		return grantedAuthorities;
		
//		for(String role : roleList) {
//			GrantedAuthority authority = new GrantedAuthority() {
//				
//				@Override
//				public String getAuthority() {
//					return role;
//				}
//			};
//			grantedAuthorities.add(authority);
//		}
		
		
	}

	@Override
	public String getPassword() {
		return user.getUser_password();
	}

	@Override
	public String getUsername() {
		return user.getUser_name();
	}
	/*
	 * 계정 만료 여부
	 * true : 만료 안됨
	 * false : 만료
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	/*
	 * 계정 잠김 여부
	 * true : 잠기지 않음 
	 * false : 잠김 
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	/*
	 * 비밀번효 만료
	 * true : 만료 안됨
	 * false : 만료됨 
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	/*
	 * 사용자 활성화 여부
	 * true : 활성화
	 * false: 비활성화 
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attribute;
	}

	@Override
	public String getName() {
		return user.getUser_name();
	}

}
