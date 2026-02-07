package com.pro.pair.cart.model.dto;

import java.sql.Date;

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
	
	
}
