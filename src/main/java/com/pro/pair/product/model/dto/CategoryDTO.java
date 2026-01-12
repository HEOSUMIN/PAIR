package com.pro.pair.product.model.dto;

import lombok.Data;

@Data
public class CategoryDTO {
	private String categoryNo;		//카테고리코드 
	private String categoryNm;		//카테고리이름 
	private String upCategoryNo;	//상위카테고리코드
	private int lvl;				//레벨 
	private int sortOrder;			//정렬순서 
	private char useYn;				//사용여부 
	
}