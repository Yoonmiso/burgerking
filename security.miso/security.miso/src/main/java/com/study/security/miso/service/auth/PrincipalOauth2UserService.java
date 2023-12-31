package com.study.security.miso.service.auth;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.study.security.miso.domain.User.User;
import com.study.security.miso.domain.User.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j //log 역할 syso 로 찍어보는 것과 동일 
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info(">>>> ClientRegistration: {}", userRequest.getClientRegistration()); //@Slf4j로 값이들어가는 거임
		log.info(">>>> oAuth2User: {}", oAuth2User);
		
		String provider = null;
		ClientRegistration clientRegistration = userRequest.getClientRegistration(); //네이버 구글 공통적으로 있었음
		Map<String, Object> attributes = oAuth2User.getAttributes(); //유저 정보 
		
		provider = clientRegistration.getClientName(); //네이버 구글 
	
		User user = getOauth2User(provider, attributes);
		return new PrincipalDetails(user, attributes);
	}
	
	private User getOauth2User(String provider, Map<String, Object> attributes) throws OAuth2AuthenticationException{
		User user= null;
		String oauth2_id = null;
		String id = null;
		Map<String,Object> response = null;
		
		if(provider.equalsIgnoreCase("google")) {
			response= attributes;
			id = (String)response.get("sub");
		}else if(provider.equalsIgnoreCase("naver")) {
			response=(Map<String, Object>) attributes.get("response");
			id = (String)response.get("id");	
		}else {
			throw new OAuth2AuthenticationException("DATABASE Error!");
		}
		oauth2_id = provider + "_" + id;
		
		try {
			user = userRepository.findOauth2USerByUserName(oauth2_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2AuthenticationException("DATABASE Error!");
		}
		
		if(user == null) {
			user = User.builder()
					.user_name((String)response.get("name"))
					.user_email((String)response.get("email"))
					.user_id(oauth2_id)
					.oauth2_id(oauth2_id)
					.user_password(new BCryptPasswordEncoder().encode(id))
					.user_roles("ROLE_USER")
					.user_provider(provider)
					.build();	
			try {
				userRepository.save(user);
				user = userRepository.findOauth2USerByUserName(oauth2_id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2AuthenticationException("DATABASE Error!");

			}
					
		}
		
		
		return null;
	}
}
