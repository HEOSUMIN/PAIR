package com.pro.pair.handler;

import java.io.IOException;
import java.net.URLEncoder;

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
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private MemberMapper memberMapper;

	@Autowired
	public LoginFailureHandler(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		log.info("onAuthenticationFailure");

		log.info("요청 URI = {}", request.getRequestURI());
		log.info("exception type = {}", exception.getClass().getSimpleName());
		log.info("exception message = {}", exception.getMessage());

		String errorTitle;

		if (exception instanceof BadCredentialsException) {
			errorTitle = "아이디 또는 비밀번호가 일치하지 않습니다";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			errorTitle = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다";
		}else {
			errorTitle = "로그인 요청을 처리할 수 없습니다";
			
		}
			
//			else if (exception instanceof UsernameNotFoundException) {
//			errorTitle = "존재하지 않는 계정입니다";
//		} else {
//			errorTitle = "로그인 요청을 처리할 수 없습니다";
//		}
//
		String encodedTitle = URLEncoder.encode(errorTitle, "UTF-8");
		//request.getRequestDispatcher("/member/signin?errorTitle=" + encodedTitle).forward(request, response);
		response.sendRedirect("/member/signin?errorTitle=" + encodedTitle);
	} 

}
