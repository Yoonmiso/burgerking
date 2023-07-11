package com.study.security.miso.web.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.util.StandardCharset;
import com.study.security.miso.service.notice.NoticeService;
import com.study.security.miso.service.notice.NoticeServiceImpl;
import com.study.security.miso.web.dto.CMResponseDto;
import com.study.security.miso.web.dto.notice.AddNoticeReqDto;
import com.study.security.miso.web.dto.notice.GetNoticeRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notice")
@Slf4j
@RequiredArgsConstructor
public class NoticeRestController {
	
	private final NoticeService noticeService;
	
	@Value("${file.path}")
	private String filePath;
	
	
	@PostMapping("")
	public ResponseEntity<?> addNotice(AddNoticeReqDto addNoticeReqDto) {
		log.info(">>>{}:" , addNoticeReqDto);
		log.info(">>>fileName: {}", addNoticeReqDto.getFile().get(0).getOriginalFilename());
		log.info(">>>filePath: {}", filePath);
		
		int noticeCode = 0;
		try {
			noticeCode = noticeService.addNotice(addNoticeReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMResponseDto<>(-1, "Failed", noticeCode));
		}
		
		return ResponseEntity.ok(new CMResponseDto<>(1, "complete creation", noticeCode));
	}
	
	//게시물을 그냥 보는 것 
	@GetMapping("/{noticeCode}")
	public ResponseEntity<?> getNotice(@PathVariable int noticeCode) {
		
		GetNoticeRespDto getNoticeRespDto = null;
		try {
			getNoticeRespDto =  noticeService.getNotice(null, noticeCode);
			if(getNoticeRespDto == null) {
				return ResponseEntity.ok().body(new CMResponseDto<>(-1, "database error",getNoticeRespDto));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new CMResponseDto<>(-1, "database error",getNoticeRespDto));
		}
		return ResponseEntity.ok().body(new CMResponseDto<>(1, "success",getNoticeRespDto));
	}
	
	
	// 이전 다음 버튼을 눌렀을때 둘어가는곳 
	@GetMapping("/{flag}/{noticeCode}")
	public ResponseEntity<?> getNotice(@PathVariable String flag, @PathVariable int noticeCode) {
		GetNoticeRespDto getNoticeRespDto = null;
		if(flag.equals("pre") || flag.equals("next")) {
			try {
				getNoticeRespDto =  noticeService.getNotice(flag, noticeCode);
				if(getNoticeRespDto == null) {
					return ResponseEntity.ok().body(new CMResponseDto<>(-1, "database error",getNoticeRespDto));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.ok().body(new CMResponseDto<>(-1, "database error",getNoticeRespDto));
			}
			
		} else {
			return ResponseEntity.ok().body(new CMResponseDto<>(1, "request failed",null));
		}
		return ResponseEntity.ok().body(new CMResponseDto<>(1, "success",getNoticeRespDto));
	}
	
	//파일 다운로드 통신을 위해 보낼때 헤더스에 넣어서 타입만 맞춰서 보낸다. 
	@GetMapping("/file/download/{fileName}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
		Path path = Paths.get(filePath + "notice/" + fileName);
		String contentType = Files.probeContentType(path);
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment")
											.filename(fileName, StandardCharset.UTF_8)
											.build());
		headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@GetMapping("/list/{page}")
	public ResponseEntity<?> getNoticeList(@PathVariable int page, @RequestParam String searchFlag, @RequestParam String searchValue ){
		List<GetNoticeListDto> listDto = new ArrayList<>();
		try {
			listDto = noticeService.getNoticeList(page, searchFlag, searchValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok().body(new CMResponseDto<>(-1, "database error", listDto));
		}
	return ResponseEntity.ok().body(new CMResponseDto<>(1, "success", listDto));
}
}
