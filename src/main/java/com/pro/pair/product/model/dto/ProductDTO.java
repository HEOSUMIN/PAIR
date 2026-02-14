package com.pro.pair.product.model.dto;

import java.util.Date;
import java.util.List;

import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class ProductDTO {
	
	private int prodNo;					//상품번
	private String prodNm;				//상품명
	private String brandCd;				//브랜드코드
	private String brandNm;				//브랜드명
	private int categoryNo;				//카테고리번호
	private int price;					//가격
	private int discountRate;			//할인율 
	private int salePrice;				//할인가
	private String prodDesc;			//상품설명
	private String prodDetailContent;	//상품상세설명
	private Date insDt;					//상품등록일자 
	private Date uptDt;					//상품수정일자	
	private char useYn;					//사용여부
	private String tag;					//추천음식태그 
	
	private CategoryDTO category;
	private BrandDTO brand;
	
	private List<AttachmentDTO> attachmentList;
	private OptionCombDTO optionComb;
	
	private List<ProductDTO> prodCombs; 	//추천조합상품
//	
//	
//	public ProductDTO() {}
//	
//	public ProductDTO(ProductDTO prod, List<ProductDTO> prodCombs) {
//		this.prodNo = prod.getProdNo();
//		this.prodNm = prod.getProdNm();
//		this.brandCd = prod.getBrandCd();
//		this.brandNm = prod.getBrandNm();
//		this.categoryNo = prod.getCategoryNo();
//		this.discountRate = prod.getDiscountRate();
//		this.salePrice = prod.getSalePrice();
//		this.prodDesc = prod.getProdDesc();
//		this.prodDetailContent = prod.getProdDetailContent();
//		this.insDt = prod.getInsDt();
//		this.uptDt = prod.getUptDt();
//		this.useYn = prod.getUseYn();
//		this.tag = prod.getTag();
//		
//		this.category = prod.getCategory();
//		this.brand = prod.getBrand();
//		this.attachmentList = prod.getAttachmentList();
//		this.optionComb = prod.getOptionComb();
//		this.prodCombs = prodCombs;
//		
//	}
}
