package com.pro.pair.cart.model.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
	private int orderItemNo;
	private String orderNo;
	private int optCombNo;
	private int orderQuantity;
	private int orderAmount;
	
}
