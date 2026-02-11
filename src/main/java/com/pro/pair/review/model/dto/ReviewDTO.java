package com.pro.pair.review.model.dto;

import java.sql.Date;
import java.util.List;

import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import lombok.Data;

@Data
public class ReviewDTO {
	private int reviewNo;
	private int optCombNo;
	private String orderItemNo;
	private String memberId;
	private String revwContent;
	private Date revwRegDate;
	private int revwRatings;
	
	private OptionCombDTO optionComb;
	private ProductDTO product;
	private List<AttachmentDTO> attachmentList;
}
