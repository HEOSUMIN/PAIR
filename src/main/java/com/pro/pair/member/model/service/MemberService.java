package com.pro.pair.member.model.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.member.model.dto.MemberDTO;
import com.pro.pair.review.model.dto.ReviewDTO;

public interface MemberService extends UserDetailsService  {
	
	/* 아이디 중복 검사  */  
	int checkId(String memberId);
	
	/* 회원가입 */
	boolean signUpMember(MemberDTO member) throws Exception;
	
	List<OrderDTO> getMemberOrderList(String memberId);
	
	int getMemberOrderCountByDlvrStatus(String memberId, String dlvrStatus);
	
	OrderDTO getMemberOrderDetails(String memberId, String orderNo);

	List<OrderDTO> getOptionListByOrderNo(String orderNo);
	
	int getTotalOrderAmountByOrderNo(String orderNo);
	
	List<OrderDTO> getItemsToPostAReview(String memberId);
	
	List<ReviewDTO> getMemberReviewPosts(String memberId);
	
	OrderDTO getOrderInfoToReview(String memberId, int orderItemNo, int optCombNo);
	
	int getWritableReviewCount(String memberId); 
}
