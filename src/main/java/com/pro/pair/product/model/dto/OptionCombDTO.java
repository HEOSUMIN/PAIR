package com.pro.pair.product.model.dto;


import lombok.Data;

@Data
public class OptionCombDTO {
	private int optCombNo;
	private int prodNo;
	private int optAddPrice;
	private int optStockQty;
	private String optManageNm;
	private char saleStatus;			//판매여부
	
}
