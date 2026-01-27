package com.pro.pair.cart.model.dto;

import java.sql.Date;
import java.util.List;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class CartDTO {
	private int cartNo;
	private String memberId;
	private int prodNo;
	private int optCombNo;
	private int quantity;
	private Date insDt;
	private Date uptDt;
	
	private ProductDTO product;
	private OptionCombDTO optionComb;
	private CategoryDTO category;
	private BrandDTO brand;
	
	private List<AttachmentDTO> attachmentList;
}
