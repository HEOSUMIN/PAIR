package com.pro.pair.member.model.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface MemberService extends UserDetailsService  {
	
	/* 회원가입 */  
	int checkId(String memberId);
	

}
