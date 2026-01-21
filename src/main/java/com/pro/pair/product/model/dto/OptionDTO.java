package com.pro.pair.product.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class OptionDTO {

	private int optNameNo;
	private String optNameNm;
	private int prodNo;
	private int sortOrder;
	
	private List<OptionValueDTO> values;
	private List<OptionCombDTO> combs;
	
}
