package com.study.security.miso.service.notice;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.security.miso.domain.notice.Notice;
import com.study.security.miso.domain.notice.NoticeFile;
import com.study.security.miso.domain.notice.NoticeRepository;
import com.study.security.miso.web.dto.notice.AddNoticeReqDto;
import com.study.security.miso.web.dto.notice.GetNoticeRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
	
	@Value("${file.path}")
	private String filePath;
	
	private final NoticeRepository noticeRepository;
	
	@Override
	public int addNotice(AddNoticeReqDto addNoticeReqDto) throws Exception {
		
		Predicate<String> predicate = (filename) -> !filename.isBlank(); // 리턴값이 true false
		
		Notice notice = null;
		
		notice = Notice.builder()
				.notice_title(addNoticeReqDto.getNoticeTitle())
				.user_code(addNoticeReqDto.getUserCode())
				.notice_content(addNoticeReqDto.getIr1())
				.build();
				
		noticeRepository.saveNotice(notice);
		
		if(predicate.test(addNoticeReqDto.getFile().get(0).getOriginalFilename())) {//파일이 존재하는지를 알려
			
			List<NoticeFile> noticeFiles = new ArrayList<>();
			
			
			for(MultipartFile file : addNoticeReqDto.getFile()) {
					String originFileName =file.getOriginalFilename();
					String tempFileName = UUID.randomUUID().toString().replaceAll("-", "") + "-" + originFileName;
					
					Path uploadPath = Paths.get(filePath, "notice/" + tempFileName); // 경로 객체를 만들어줌 
					
					File f = new File(filePath + "notice"); //notice라는것이 업승면 새로운 폴더를 생성해 거기에 파일을 넣는다. 
					if(!f.exists()) {
						f.mkdir(); //파일을 만든것 
					}
					
					Files.write(uploadPath, file.getBytes());
					
					noticeFiles.add(NoticeFile.builder()
							.notice_code(notice.getNotice_code())
							.file_name(tempFileName)
							.build());
					
					
					
				}
				noticeRepository.saveNoticeFiles(noticeFiles);
			};
		return notice.getNotice_code();
	}

	@Override
	public GetNoticeRespDto getNotice(String flag, int noticeCode) throws Exception {
		GetNoticeRespDto getNoticeRespDto = null;
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("flag", flag);
		reqMap.put("notice_code", noticeCode);
		
		List<Notice> notices =  noticeRepository.getNotice(reqMap);
		if(!notices.isEmpty()) {
			List<Map<String, Object>> downloadFiles = new ArrayList<>();
			notices.forEach(notice -> {
				Map<String, Object> fileMap = new HashMap<>();
				fileMap.put("fileCode", notice.getFile_code());
				String fileName = notice.getFile_name();
				if(fileName != null) {
					fileMap.put("fileCode", notice.getFile_code());
					fileMap.put("fileOriginName", fileName.substring(fileName.indexOf("_")+1));
					fileMap.put("fileTempName", fileName);
				}
				downloadFiles.add(fileMap);
				});
			Notice firstNotice = notices.get(0);
			getNoticeRespDto = GetNoticeRespDto.builder()
						.noticeCode(firstNotice.getNotice_code())
						.noticeTitle(firstNotice.getNotice_title())
						.userCode(firstNotice.getUser_code())
						.userId(firstNotice.getUser_id())
						.createDate(firstNotice.getCreate_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						.noticeCount(firstNotice.getNotice_count())
						.noticeContent(firstNotice.getNotice_content())
						.downloadFiles(downloadFiles)
						.build();
		}
		return null;
	}

	@Override
	public List<Object> getNoticeList(int page, String searchFlag, String searchValue) throws Exception {
		int index = (page-1)*10;
		Map<String, Object> map = new HashMap<>();
		map.put("index", index);
		map.put("searchFlag", searchFlag);
		map.put("searchValue", searchValue == null ? "" : searchValue);
		
		List<GetNoticeListRespDto> list  = new ArrayList<>();
		
		noticeRepository.getNoticeList(map).forEach(notice -> {
			list.add(notice.toListDto());
		});
		return null;
	}
	
	
	
}
