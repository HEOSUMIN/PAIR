package com.pro.pair.product.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class OptionCombDTO {
	private int optCombNo;
	private int prodNo;
	private int optAddPrice;
	private int optStock;
	private String optManageNm;
	private Boolean isSoldOut;
	
}
