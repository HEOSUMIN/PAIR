package com.pro.pair.member.model.dto;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class WishListDTO {
	
	private String memberId;
	private int prodNo;
	
	private CategoryDTO category;
	private BrandDTO brand;
	private AttachmentDTO attachmentList;
}
