package com.pro.pair.member.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pro.pair.member.model.dto.MemberDTO;
import com.pro.pair.member.model.dto.UserImpl;
import com.pro.pair.member.model.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberservice;
	private final MessageSource messageSource;
	private final PasswordEncoder passwordEncoder;

	public MemberController(MemberService memberservice, MessageSource messageSource, PasswordEncoder passwordEncoder) {
		this.memberservice = memberservice;
		this.messageSource = messageSource;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 로그인
	 */
	@GetMapping("/signin")
	public String signInForm(@AuthenticationPrincipal UserImpl user, @RequestParam(required = false) String errorTitle,
			Model model, Locale locale, HttpSession session) {
		if (errorTitle != null) {
			model.addAttribute("errorTitle", errorTitle);
			//model.addAttribute("signInMessageText", session.getAttribute("signInMessageText"));

			//session.removeAttribute("signInMessageTitle");
			//session.removeAttribute("signInMessageText");
		}

		if (user != null) {
			model.addAttribute("loginAccessDenied", messageSource.getMessage("loginAccessDenied", null, locale));
			return "/common/denied";
		}
		return "/member/signin";
	}
//
//	@PostMapping("/signin")
//	public String singnInMember(@RequestParam(required = false) String errorTitle, @RequestParam(required = false) String errorText,
//			@RequestParam(required = false) String resetPasswordRequired, HttpServletRequest request, HttpSession session,
//			RedirectAttributes rttr, Locale locale) {
//		
//		String uri = request.getHeader("Referer");	//사용자 이전 경로 
//		String loginMember = (String) session.getAttribute("loginMember");
//		
//		log.info("22222!요청 loginMember : {}", loginMember);
//		
//		if(loginMember == null) {
//			if(uri != null && !(uri.contains("/signin"))) { //돌아가야 할 이전 경로가 존재하고, 사용자가 직접 로그인페이지를 요청한 것이 아닌 경우
//				request.getSession().setAttribute("prevPage", uri); //이전 경로를 session상에 저장하여 LoginSuccessHandler 통해 처리
//			}
//		}
//		
//		log.info("!!!!!!요청 uri : {}", uri);
//	
//		
//		/*
//		 * 로그인 실패
//		 * : LoginFailureHandler로부터 @RequestParam 어노테이션 통해 넘어온 에러메시지 출력
//		 */
//		log.info("!!!!!! errorTitle : {}", errorTitle);
//		if(errorTitle != null) {
//			log.info("!!!!!!2@@@@ : {}", errorTitle);
//			rttr.addFlashAttribute("signInMessageTitle", errorTitle);
//			rttr.addFlashAttribute("signInMessageText", errorText);
//			//return "redirect:/member/signin";
//		}
//		
//		return uri;
//		
//	}

	/**
	 * 회원가입
	 */
	@GetMapping("/signup")
	public String signUpForm() {

		return "/member/signup";
	}

	/**
	 * 회원가입
	 */
	@PostMapping("/signup")
	public Object signUpMember(@Validated @ModelAttribute("member") MemberDTO member, BindingResult bindingResult,
			@RequestParam Map<String, String> params, RedirectAttributes rttr, Model model, Locale locale)
			throws Exception { // BindingResult는 유효성 검사 대상 객체 바로 뒤에 선언
		/*
		 * signUpValidator.validate(member, bindingResult);
		 * 
		 * if(bindingResult.hasErrors()) { //검증 오류 발생 시 입력 폼으로 리턴
		 * log.info("검증 오류 발생 : {}", bindingResult); return "signup"; }
		 */

		/* 회원가입 성공 로직 */
		String phone = params.get("phoneA") + params.get("phoneB") + params.get("phoneC");
		String address = params.get("postalCode") + "$" + params.get("address") + "$" + params.get("detailAddress");
		member.setPhone(phone);
		member.setAddress(address);
		member.setStatus('Y');
		log.info("member: {}", member);
		memberservice.signUpMember(member);

		/*
		 * 리다이렉트 시에는 요청이 새로 생겨나는 것이므로 RedirectAttributes 사용 메시지 목록을 리터럴리하게 쓰지 않고 따로 목록화
		 * 해서 관리되도록 ContextConfiguration에 bean으로 등록 상단에 MessageSource 타입에 대하여 의존성 주입
		 * 결과적으로 request scope 안에 successMessage가 담기는 것
		 */
		rttr.addFlashAttribute("signUpMessage", messageSource.getMessage("signUpMember", null, locale));
		log.info("성공 로직 실행 완료");
		// mailService.sendVerificationEmail(member);
		return "redirect:/";
	}

	/**
	 * 아이디 중복 검사
	 */
	@ResponseBody // ajax 통신에서 JSON 응답을 치르기 위해서는 어노테이션 추가, spring-web의 경우 jackson-databind 등 기본적으로
					// 추가돼 있는 의존성들을 고려하여야 함
	@PostMapping(value = "/checkId", produces = "application/json; charset=UTF-8")
	public int checkId(String memberId) {
		int result = memberservice.checkId(memberId);
		log.info("결과: {}", result);

		return result;
	}

}
