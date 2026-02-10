package com.pro.pair.cart.model.dto;

import java.sql.Date;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class OrderDTO {
	private String orderNo;
	private String memberId;
	private Date orderDate;
	private String rcvrName;
	private String rcvrPhone;
	private String rcvrAddress;
	private String dlvrReqMsg;
	private String dlvrStatus;
	
	private OrderItemDTO orderItem;
	private ProductDTO product;
	private PaymentDTO payment;
	private DeliveryDTO delivery;
	private BrandDTO brand;
	private AttachmentDTO attachmentList;
	private OptionCombDTO optionComb;
	
	
	
}
