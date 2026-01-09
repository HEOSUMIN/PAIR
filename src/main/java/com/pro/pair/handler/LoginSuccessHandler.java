package com.pro.pair.handler;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pro.pair.member.model.dao.MemberMapper;
import com.pro.pair.member.model.dto.UserImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@SessionAttributes("loginMember")
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private MemberMapper memberMapper;
	
	@Autowired
	public LoginSuccessHandler(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.info("onAuthenticationSuccess");
		
		UserImpl loginMember = (UserImpl) authentication.getPrincipal();
		String username = loginMember.getMemberId();
		
		request.getSession().setAttribute("loginMember", username); //session에 현재 로그인한 회원 정보 저장
		
		//memberMapper.updateLatestLoginDate(username); //최근 로그인 일시 업데이트
		
	
		HttpSession session = request.getSession();
		
	
		/* 3-1. 로그인 상황별 이동 경로 저장 */
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); //Spring Security가 요청을 가로채거든 URI 정보가 저장되는 객체
		
		String prevPage = (String) session.getAttribute("prevPage"); //MemberController로부터 저장돼 넘어온 사용자의 이전 경로
		if(prevPage != null) session.removeAttribute("prevPage");
		log.info("prevPage : {}", prevPage);
		
		String uri = request.getContextPath(); //uniform resource identifier
		
		if(savedRequest != null) { //상황A. 접근한 페이지가 Security에 의해 로그인 인증을 필요로 한 경우
			uri = savedRequest.getRedirectUrl();
			
		} else if(prevPage != null) { //상황B. 사용자가 직접 로그인페이지를 요청한 경우
			uri = prevPage;
			log.info("prevPage : {}", prevPage);
		}
		
		log.info("요청 uri : {}", uri);
		
		response.sendRedirect("/");
		
		
		/*
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		log.info("ROLE : {}", roles);
		
		if(roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/dashboard");
		}else {
			response.sendRedirect("/");
		}*/
		
	}
	
	
	

}
