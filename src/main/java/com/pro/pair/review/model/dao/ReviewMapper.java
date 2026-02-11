package com.pro.pair.review.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

@Mapper
public interface ReviewMapper {
	
	int checkCurrReviewNo();

	int postAReview(ReviewDTO reviewDTO); 
	
	int attachReviewImages(AttachmentDTO attachment);
}
