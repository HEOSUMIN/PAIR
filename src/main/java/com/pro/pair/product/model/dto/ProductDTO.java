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
	
	private CategoryDTO category;
	private BrandDTO brand;
	
	private List<AttachmentDTO> attachmentList;
	private OptionCombDTO optionComb;
}
