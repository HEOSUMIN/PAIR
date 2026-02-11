package com.pro.pair.review.model.service;

import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;


public interface ReviewService {
	
	int checkCurrReviewNo();
	
	int postAReview(ReviewDTO reviewDTO);
	
	int attachReviewImages(AttachmentDTO attachment);

}
