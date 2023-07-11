package com.study.security.miso.domain.notice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.study.security.miso.web.dto.notice.GetNoticeRespDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	private int notice_code;
	private String notice_title;
	private int user_code;
	private String user_id;
	private String notice_content;
	private int notice_count;
	
	private int file_code;
	private String file_name;
	private LocalDateTime create_date;
	
	private int  total_notice_count;
	
	public GetnoticeListRespDto toListDto() {
		return GetNoticeListRespDto.builder()
				.noticeCode(notice_code)
				.noticeTitle(notice_title)
				.userId(user_id)
				.createDate(create_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.noticeCount(notice_count)
				.totalnoticeCount(total_notice_count)
				.build();
		
				
	}
}
