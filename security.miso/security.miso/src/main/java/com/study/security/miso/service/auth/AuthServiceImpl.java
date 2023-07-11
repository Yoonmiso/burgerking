package com.study.security.miso.service.auth;

import org.springframework.stereotype.Service;

import com.study.security.miso.domain.User.User;
import com.study.security.miso.domain.User.UserRepository;
import com.study.security.miso.web.dto.auth.UserNameCheckReqDto;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	
	@Override
	public boolean checkUsername(UserNameCheckReqDto userNameCheckReqDto) throws Exception {
		
		return userRepository.findUSerByUserName(userNameCheckReqDto.getUsername()) == null;
	}

	@Override
	public boolean signup() throws Exception {
		return false;
	}
	
}
