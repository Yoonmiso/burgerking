package com.study.security.miso.web.dto.notice;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetNoticeRespDto {

	private int noticeCode;
	private String noticeTitle;
	private String userId;
	private String createData;
	private int noticeCount;
	private int totalNoticeCount;
	
}
