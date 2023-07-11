package com.study.security.miso.web.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data

public class UserNameCheckReqDto {
	@NotBlank //빈값 안됨 
	@Size(max= 16, min= 4)  //글자수 범위 
	private String username;
}
