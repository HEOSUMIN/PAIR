package com.pro.pair.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

	/**
	 * 로그인
	 * 
	 * @return
	 */
	@GetMapping("signin")
	public String signInForm() {

		return "/member/signin";
	}

	/**
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@PostMapping
	public String signInMember(HttpServletRequest request, HttpSession session) {
		String uri = request.getHeader("Referer"); // 사용자 이전 경로

		return uri;
	}

	/**
	 * 회원가입
	 * 
	 * @return
	 */
	@GetMapping("/signup")
	public String signUpForm() {

		return "/member/signup";
	}
	
	/**
	 * 아이디 중복 검사 
	 */
	@GetMapping("/checkId")
	public int checkId(String memberId) {
		int result =0;
		
		return result;
		
	}

}
