package com.pro.pair.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.pro.pair.member.model.dao.MemberMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler{
	
/*	private MemberMapper memberMapper;
	
	@Autowired
	public LoginFailureHandler(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	*/
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("onAuthenticationFailure");
		String errorTitle = "";
		String errorText = "";
		
		log.info("발생한 exception 클래스 : {}", exception.getClass());
		
		if(exception instanceof BadCredentialsException) {
			String username = request.getParameter("username");
			log.info("username : {}", username);
			errorTitle = "아이디 또는 비밀번호가 일치하지 않습니다";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			errorTitle = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
		} else if (exception instanceof UsernameNotFoundException) {
			errorTitle = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			errorTitle = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
		} else {
			errorTitle = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
		}
		
		log.info("exception.getMessage() : {}", exception.getMessage());
		log.info("!!!!errorTitle: {}", errorTitle);
		log.info("!!!!errorTitle: {}", request);
		log.info("!!!!errorTitle: {}", response);
	        
		request.getRequestDispatcher("/member/signin?errorTitle=" + errorTitle).forward(request, response);
		
		/*
		if(exception instanceof BadCredentialsException) {
			String username = request.getParameter("username");
			log.info("username : {}", username);
			errorTitle = "아이디 또는 비밀번호가 일치하지 않습니다";
			errorText = "5회 이상 실패 시 계정이 잠금 처리됩니다";
			
		} else if(exception instanceof UsernameNotFoundException) {
			errorTitle = "존재하지 않는 계정입니다";
			errorText = "";
		} else {
			errorTitle = "로그인 요청을 처리할 수 없습니다";
			errorText = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요";
			
			if(exception.getMessage().indexOf("비활성화") > 0) {
				errorTitle = "이메일 인증 전 비활성화 상태인 계정입니다";
				errorText = "본인인증메일을 확인하세요";
			}
		}
		
		
		log.info("exception.getMessage() : {}", exception.getMessage());
		log.info("커스텀 exception 메시지 : {}", errorTitle + errorText);
		
		request.getRequestDispatcher("/member/signin?errorTitle=" + errorTitle + "&errorText=" + errorText).forward(request, response);
		
		*/
		
		//errorTitle = URLEncoder.encode(errorTitle, "UTF-8"); /* 한글 인코딩 깨진 문제 방지 */
		//setDefaultFailureUrl("/member/signin?error=true&exception="+errorTitle);
		//super.onAuthenticationFailure(request, response, exception);
	
	}
	
	

}
