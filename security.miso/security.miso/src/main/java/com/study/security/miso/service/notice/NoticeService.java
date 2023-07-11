package com.study.security.miso.service.notice;

import java.util.List;

import com.study.security.miso.web.dto.notice.AddNoticeReqDto;
import com.study.security.miso.web.dto.notice.GetNoticeRespDto;

public interface NoticeService {
	public int addNotice(AddNoticeReqDto addNoticeReqDto) throws Exception;
	
	public GetNoticeRespDto getNotice(String flag, int noticeCode) throws Exception;
	
	public List<Object> getNoticeList(int page, String searchFlag, String searchValue) throws Exception;
}
