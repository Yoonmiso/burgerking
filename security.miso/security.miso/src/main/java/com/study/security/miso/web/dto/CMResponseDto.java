package com.study.security.miso.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CMResponseDto<T> {
	private int code;
	private String message;
	private T data; 
}
