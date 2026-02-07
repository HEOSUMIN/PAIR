package com.pro.pair.cart.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PaymentDTO {

	private String paymentNo;
	private String orderNo;
	private String paymentMethod;
	private int paymentAmount;
	private Date paymentDateTime;
}
