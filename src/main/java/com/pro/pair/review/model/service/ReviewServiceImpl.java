package com.pro.pair.review.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pro.pair.review.model.dao.ReviewMapper;
import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService{
	
	private ReviewMapper reviewMapper;
	
	@Autowired
	public ReviewServiceImpl(ReviewMapper reviewMapper) {
		this.reviewMapper = reviewMapper;
	}

	@Override
	public int postAReview(ReviewDTO reviewDTO) {
		return reviewMapper.postAReview(reviewDTO);
	}

	@Override
	public int checkCurrReviewNo() {
		return reviewMapper.checkCurrReviewNo();
	}

	@Override
	public int attachReviewImages(AttachmentDTO attachment) {
		return reviewMapper.attachReviewImages(attachment);
	}

}
