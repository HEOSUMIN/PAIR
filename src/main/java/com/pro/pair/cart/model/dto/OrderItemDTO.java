package com.pro.pair.cart.model.dto;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class OrderItemDTO {
	private int orderItemNo;
	private String orderNo;
	private int optCombNo;
	private int orderQuantity;
	private int orderAmount;
	
	private ProductDTO product;
	private BrandDTO brand;
	private AttachmentDTO attachmentList;
	private OptionCombDTO optionComb;
	
}
