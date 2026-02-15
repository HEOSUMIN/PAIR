package com.pro.pair.product.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.pro.pair.paging.model.dto.Criteria;
import com.pro.pair.paging.model.dto.ItemCriteria;
import com.pro.pair.paging.model.dto.PageDTO;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.OptionDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.product.model.service.ProductService;
import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Controller
public class productController {

	// 썸네일 크기
	public static final int THUMB_WIDTH_SIZE = 540;
	public static final int THUMB_HEIGHT_SIZE = 540;

	private final ProductService productService;
	private final MessageSource messageSource;

	public productController(ProductService productService, MessageSource messageSource) {
		this.productService = productService;
		this.messageSource = messageSource;
	}

	/*
	 * 관리자-상품등록
	 */
	@GetMapping("/admin/product/add")
	public void addProduct(Model model) {
		// 카테고리 리스트 출력
		List<CategoryDTO> category = productService.getCategoryList();
		model.addAttribute("category", category);

		// 브랜드 리스트 출력
		List<BrandDTO> brand = productService.getBrandList();
		model.addAttribute("brand", brand);
	}

	/*
	 * 관리자-카테고리 선택시 하위 카테고리 조회
	 */
	@GetMapping(value = "/option", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<CategoryDTO> checkSubCategoty(@RequestParam("categoryNo") int categoryNo, Model model) {
		List<CategoryDTO> subCategoryList = productService.getSubCategoryList(categoryNo);
		model.addAttribute("subCategoryList", subCategoryList);
		return subCategoryList;
	}

	/*
	 * 관리자-상품등록
	 */
	@PostMapping(value = "/admin/product/add", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@ResponseBody
	public ModelAndView addProduct(@RequestPart("params") Map<String, Object> params,
			@RequestPart("optionData") Map<String, Object> optionData,
			@RequestParam(value = "files", required = false) List<MultipartFile> files, HttpServletRequest request,
			HttpServletResponse response, Locale locale) {

		log.info("params: {}", params);
		log.info("optionData: {}", params);

		/* jsonView 적용 */
		ModelAndView mv = new ModelAndView();
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		mv.setView(jsonView);

		/* 상품추가 */
		int categoryNo = Integer.parseInt(params.get("category").toString());
		String brandCd = params.get("brand").toString();
		String prodNm = params.get("prodNm").toString();
		String prodDesc = params.get("prodDesc").toString();
		int price = Integer.parseInt(params.get("prodPrice").toString());
		int discountRate = Integer.parseInt(params.get("discountRate").toString());
		int salePrice = (int) (price * (discountRate * 0.01));
		String prodDetailContent = params.get("prodDetailContent").toString();

		// ProductDTO 객체에 값으로 설정
		ProductDTO product = new ProductDTO();
		product.setCategoryNo(categoryNo);
		product.setBrandCd(brandCd);
		product.setProdNm(prodNm);
		product.setProdDesc(prodDesc);
		product.setPrice(price);
		product.setDiscountRate(discountRate);
		product.setSalePrice(salePrice);
		product.setProdDetailContent(prodDetailContent);

		int addProdRslt = productService.addProduct(product);

		// 현재 상품번호 조회
		int currProdNo = productService.checkCurrProdNo();

		/* 옵션추가 */
		int addOptRslt = 0;
		if (params.get("optionCount") != null && !params.get("optionCount").toString().trim().isEmpty()) {
			addOptRslt = productService.addOption(currProdNo, optionData);
		} else {
			addOptRslt = 1;
		}

		log.info("상품 옵션 insert end");

		/* 썸네일 추가 */
		String realPath = request.getSession().getServletContext().getRealPath("/");
		log.info("src/main/webapp : {}", realPath);

		String originalUploadPath = realPath + "upload" + File.separator + "product" + File.separator + "original";
		String thumbnailUploadPath = realPath + "upload" + File.separator + "product" + File.separator + "thumbnail";
		File originalDirectory = new File(originalUploadPath);
		File thumbnailDirectory = new File(thumbnailUploadPath);

		if (!originalDirectory.exists() || !thumbnailDirectory.exists()) { // 지정 폴더가 존재하지 않을 시 생성
			originalDirectory.mkdirs(); // 생성할 폴더가 하나이면 mkdir, 상위 폴더도 존재하지 않으면 한 번에 생성하란 의미로 mkdirs를 이용
			thumbnailDirectory.mkdirs();
		}

		Map<String, String> fileMap = new HashMap<>();
		List<Map<String, String>> fileList = new ArrayList<>();

		log.info("files : {}", files);

		for (MultipartFile file : files) {
			UUID uuid = UUID.randomUUID(); // 랜덤 문자 생성

			String origFileNm = file.getOriginalFilename(); // 원본파일명

			String extension = FilenameUtils.getExtension(origFileNm); // 확장자
			String randomFileNm = uuid.toString().replace("-", "") + "." + extension; // 랜덤파일명

			try {
				// 원본 크기 파일을 original 폴더에 저장
				File target = new File(originalUploadPath, randomFileNm);
				byte[] bytes = file.getBytes();
				FileCopyUtils.copy(bytes, target);

				String origFileUrl = "/upload/product/original/" + uuid.toString().replace("-", "") + "." + extension;
				fileMap.put("origFileNm", origFileNm);
				fileMap.put("saveFileNm", randomFileNm);
				fileMap.put("savePath", origFileUrl);

				// 썸네일 파일을 thumbnail 폴더에 저장
				Thumbnails.of(originalUploadPath + File.separator + randomFileNm) // 썸네일로 변환 후 저장
						.size(THUMB_WIDTH_SIZE, THUMB_HEIGHT_SIZE)
						.toFile(thumbnailUploadPath + File.separator + "thumbnail_" + randomFileNm);
				fileMap.put("thumbnailPath", "/upload/product/thumbnail/thumbnail_" + randomFileNm); // 웹서버에서 접근 가능한
																										// 형태로 썸네일의 저장
																										// 경로 작성

				fileList.add(fileMap);
				// product 객체의 AttachmentList 설정
				product.setAttachmentList(new ArrayList<AttachmentDTO>());
				List<AttachmentDTO> list = product.getAttachmentList();
				log.info("fileList size : {}", fileList.size());

				AttachmentDTO tempFileInfo = new AttachmentDTO();
				for (int i = 0; i < fileList.size(); i++) {
					tempFileInfo.setRefProdNo(currProdNo);
					tempFileInfo.setOrigFileNm(fileList.get(i).get("origFileNm"));
					tempFileInfo.setSaveFileNm(fileList.get(i).get("saveFileNm"));
					tempFileInfo.setSavePath(fileList.get(i).get("savePath"));
					tempFileInfo.setThumbnailPath(fileList.get(i).get("thumbnailPath"));

					if (i == 0) { // index 기준으로 첫번째 첨부 이미지는 메인썸네일, 그 다음은 서브썸네일에 해당
						tempFileInfo.setFileType("THUMB_MAIN");
					} else {
						tempFileInfo.setFileType("THUMB_SUB");
					}

					list.add(tempFileInfo);
				}
				productService.attachProdThumbnail(tempFileInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (addProdRslt == 1 && addOptRslt == 1) {
			String successMessage = messageSource.getMessage("productAddedSuccessfully", null, locale);
			mv.addObject("successMessage", successMessage);
		} else {
			String errorMessage = messageSource.getMessage("errorWhileAddingAProduct", null, locale);
			mv.addObject("errorMessage", errorMessage);
		}

		return mv;
	}

	/*
	 * 관리자-상품목록
	 */
	@GetMapping("admin/product/list")
	public void getProductList(@Valid @ModelAttribute("criteria") Criteria criteria, BindingResult bindingResult,
			HttpServletRequest request, Model model) {
		log.info("상품 목록 조회 - 관리자 ");
		int total = productService.getTotalNumber(criteria); // 전체
		int onSale = productService.getOnSaleNumber(criteria); // 판매중

		List<ProductDTO> productList = productService.getProductList(criteria);
		List<ProductDTO> onSaleOnly = productService.getOnSaleOnly(criteria); // 판매중

		model.addAttribute("total", total);
		model.addAttribute("onSale", onSale);

		model.addAttribute("productList", productList);
		model.addAttribute("onSaleOnly", onSaleOnly);

	}

	/*
	 * 관리자-상품상세정보 조회 및 수정
	 */
	@GetMapping("/admin/product/edit")
	public void editProductDetails(@RequestParam("no") int prodNo, Model model) {

		// 카테고리 리스트 출력
		List<CategoryDTO> category = productService.getCategoryList();
		model.addAttribute("category", category);

		// 브랜드 리스트 출력
		List<BrandDTO> brand = productService.getBrandList();
		model.addAttribute("brand", brand);

		/* 상품 상세 호출 */
		ProductDTO detail = productService.getProductDetails(prodNo);

		/* 상품 썸네일 조회 */
		AttachmentDTO mainThumb = productService.getMainThumbnailByProdNo(prodNo);
		AttachmentDTO subThumb = productService.getSubThumbnailByProdNo(prodNo);

		log.info("detail {}", detail);
		model.addAttribute("category", category);
		model.addAttribute("brand", brand);
		model.addAttribute("detail", detail);
		model.addAttribute("mainThumb", mainThumb);
		model.addAttribute("subThumb", subThumb);
	}

	/*
	 * 상품목록
	 */
	@GetMapping("/product/list")
	public void getProductListByCategory(@Valid @ModelAttribute("itemCriteria") ItemCriteria itemCriteria, HttpServletRequest request, HttpSession session, Model model) {
		String section = itemCriteria.getSection();
		log.info("요첨 section : {}", section);
		log.info("요첨 itemCriteria : {}", itemCriteria);
		itemCriteria.setSection(section); //대분류 카테고리 섹션
		
		/* 상품 수 */
		int total = productService.getTotalNumberByCriteria(itemCriteria); 
		
		
		List<ProductDTO> sortedList = productService.getProductListByCategorySection(itemCriteria);
		List<ProductDTO> productList = new ArrayList<>();
		List<AttachmentDTO> thumbnailList = new ArrayList<>();
		
		for(int i=0; i<sortedList.size(); i++) {
			int prodNo = sortedList.get(i).getProdNo();
			ProductDTO prodDetails = productService.getProductDetails(prodNo);
			productList.add(prodDetails);
			AttachmentDTO mainThumb = productService.getMainThumbnailByProdNo(prodNo);
			thumbnailList.add(mainThumb);
		}
		
		log.info("productList : {}", productList);
		
		
		model.addAttribute("section", section == null || section == "" ? "전체 상품" : section);
		model.addAttribute("total", total);
		model.addAttribute("productList",productList);
		model.addAttribute("thumbnailList",thumbnailList);
		model.addAttribute("requestURI", request.getRequestURI());
		model.addAttribute("pageMaker", new PageDTO(total, 10, itemCriteria));

	}
	
	/*
	 * 상품상세페이지
	 */
	@GetMapping("/product/details")
	public void getProductDetails(@RequestParam("no") int prodNo, HttpSession session, Model model) {
		/* 최근 본 상품 */
		List<Integer> recentlyViewed = (List<Integer>) session.getAttribute("recentlyViewed");
		if(recentlyViewed == null) recentlyViewed = new ArrayList<>();
		if(recentlyViewed.size() == 0) {
			recentlyViewed.add(prodNo);
		} else {
			if(recentlyViewed.indexOf(prodNo)== -1) recentlyViewed.add(prodNo); //중복 방지
		}
		log.info("recently viewed items: {}", recentlyViewed);
		session.removeAttribute("recentlyViewed");
		session.setAttribute("recentlyViewed", recentlyViewed);
		
		session.removeAttribute("orderItem");	//주문 요청 시마다 주문목록 session 갱신
		
		/* 상품 상세 정보 호출 */
		ProductDTO detail = productService.getProductDetails(prodNo); //getProductDetails()는 전역적으로 사용되고 있어 상세페이지 조회용을 별도로 구분
		
		/* 상품 썸네일 조회 */
		AttachmentDTO mainThumb = productService.getMainThumbnailByProdNo(prodNo);
		AttachmentDTO subThumb = productService.getSubThumbnailByProdNo(prodNo);
		
		/* 옵션 조회 */
		List<OptionDTO> option = productService.getOptionListByProdNo(prodNo);
		
		/* 옵션 상세 조회 */
		List<OptionCombDTO> optionDetails = productService.getOptionDetailsListByProdNo(prodNo);
		
		/* 리뷰 */
		List<ReviewDTO> reviewList = productService.getReviewListByProdNo(prodNo);
		
		
		
		log.info("option: {}" ,option);
		log.info("optionDetails: {}" ,optionDetails);
		
		model.addAttribute("detail", detail);
		model.addAttribute("mainThumb", mainThumb);
		model.addAttribute("subThumb", subThumb);
		model.addAttribute("option", option);
		model.addAttribute("optionDetails", optionDetails);
		model.addAttribute("reviewList", reviewList);
		
	}
	
	
	@GetMapping("/product/option-combination")
	@ResponseBody
	public OptionCombDTO getOptionCombination(@RequestParam int prodNo,
	                                          @RequestParam String combName) {
	    return productService.findOptionCombByName(prodNo, combName);
	}

}












