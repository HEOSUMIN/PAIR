package com.pro.pair.review.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.member.model.dto.UserImpl;
import com.pro.pair.member.model.service.MemberService;
import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.review.model.service.ReviewService;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	//썸네일 크기
	public static final int THUMB_WIDTH_SIZE = 540;
	public static final int THUMB_HEIGHT_SIZE = 540;
	
	private MemberService memberService;
	private ReviewService reviewService;
	
	@Autowired
	public ReviewController(MemberService memberService, ReviewService reviewService) {
		this.memberService = memberService;
		this.reviewService = reviewService;
	}
	
	/*
	 * 리뷰 작성 및 수정 
	 */
	@GetMapping("/write")
	public void reviewWriteFomr(@RequestParam("order") int orderItemNo, @RequestParam("option") int optCombNo, @AuthenticationPrincipal UserImpl user, Model model) {
		OrderDTO reviewOption = memberService.getOrderInfoToReview(user.getMemberId(), orderItemNo, optCombNo);

		model.addAttribute("reviewOption", reviewOption);
	}
	
	/*
	 * 리뷰 등록
	 */
	@PostMapping(value="/write", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public String writeReview(@Validated @ModelAttribute("review") ReviewDTO reviewDTO, @AuthenticationPrincipal UserImpl user, 
			@RequestParam(value="files", required=false) List<MultipartFile> files, HttpServletRequest request, RedirectAttributes rttr, Locale locale) {
		
		log.info("reviewDTO:{}",reviewDTO);
		reviewDTO.setMemberId(user.getMemberId());
		
		reviewService.postAReview(reviewDTO);
		
		if(!files.get(0).getOriginalFilename().equals("")) {
			log.info("리뷰 사진 추가 시작");
			
			String realPath = request.getSession().getServletContext().getRealPath("/");
			log.info("src/main/webapp : {}", realPath);
			
			String originalUploadPath = realPath + "upload" + File.separator + "review" + File.separator + "original";
			String thumbnailUploadPath = realPath + "upload" + File.separator + "review" + File.separator + "thumbnail";
			File originalDirectory = new File(originalUploadPath);
			File thumbnailDirectory = new File(thumbnailUploadPath);
			
			if(!originalDirectory.exists() || !thumbnailDirectory.exists()) { //지정 폴더가 존재하지 않을 시 생성
				originalDirectory.mkdirs(); //생성할 폴더가 하나이면 mkdir, 상위 폴더도 존재하지 않으면 한 번에 생성하란 의미로 mkdirs를 이용
				thumbnailDirectory.mkdirs();
			}
			
			/* 최종적으로 request를 parsing하고 파일을 저장한 뒤 필요한 내용을 담을 리스트와 맵
			 * 파일에 대한 정보는 리스트에, 다른 파라미터의 정보는 모두 맵에 담을 것임
			 * */
			Map<String, String> fileMap = new HashMap<>();
			List<Map<String, String>> fileList = new ArrayList<>();
			int countFileAttached = 0;
			int countFileSaved = 0;
			
			for(MultipartFile file : files) {
				if(!file.isEmpty()) { //첨부 가능한 파일 개수는 최대 3개, 실제 첨부된 파일이 있는 경우에만 반복
					countFileAttached++;
					UUID uuid = UUID.randomUUID(); //랜덤 문자 생성
					
					String origFileNm = file.getOriginalFilename(); //원본파일명
					String extension = FilenameUtils.getExtension(origFileNm); //확장자
					String randomFileNm = uuid.toString().replace("-", "") + "." + extension; //랜덤파일명
					
					try {
						//원본 크기 파일을 original 폴더에 저장
						File target = new File(originalUploadPath, randomFileNm);
						byte[] bytes = file.getBytes();
						FileCopyUtils.copy(bytes, target);
						
						String origFileUrl = "/upload/review/original/" + uuid.toString().replace("-", "") + "." + extension;
						fileMap.put("origFileNm", origFileNm);
						fileMap.put("saveFileNm", randomFileNm);
						fileMap.put("savePath", origFileUrl);
						
						//썸네일 파일을 thumbnail 폴더에 저장
						Thumbnails.of(originalUploadPath + File.separator + randomFileNm) //썸네일로 변환 후 저장
						.size(THUMB_WIDTH_SIZE, THUMB_HEIGHT_SIZE)
						.toFile(thumbnailUploadPath + File.separator + "thumbnail_" + randomFileNm);
						fileMap.put("thumbnailPath", "/upload/review/thumbnail/thumbnail_" + randomFileNm); //웹서버에서 접근 가능한 형태로 썸네일의 저장 경로 작성
						
						fileList.add(fileMap);
						
						//현재 리뷰번호 조회
						int currReviewNo;
						currReviewNo = reviewService.checkCurrReviewNo(); //새 글 작성 중 부여된 번호 조회
						
						AttachmentDTO tempFileInfo = new AttachmentDTO();
						for(int i=0; i < fileList.size(); i++) {
							tempFileInfo.setRefRevwNo(currReviewNo);
							tempFileInfo.setOrigFileNm(fileList.get(i).get("origFileNm"));
							tempFileInfo.setSaveFileNm(fileList.get(i).get("saveFileNm"));
							tempFileInfo.setSavePath(fileList.get(i).get("savePath"));
							tempFileInfo.setThumbnailPath(fileList.get(i).get("thumbnailPath"));
							
							if(i == 0) { //index 기준으로 첫번째 첨부 이미지는 메인썸네일, 그 다음은 서브썸네일에 해당
								tempFileInfo.setFileType("THUMB_MAIN");
							} else {
								tempFileInfo.setFileType("THUMB_SUB");
							}
						}
						int result = reviewService.attachReviewImages(tempFileInfo);
						countFileSaved += result;
					} catch (IOException e) { e.printStackTrace(); }
				}
			}
		}
		return "redirect:/mypage/review";
		
	}
	
}
