package com.study.security.miso.service.auth;

import com.study.security.miso.web.dto.auth.UserNameCheckReqDto;

public interface AuthService {
	public boolean checkUsername(UserNameCheckReqDto userNameCheckReqDto) throws Exception;
	public boolean signup() throws Exception;
}
