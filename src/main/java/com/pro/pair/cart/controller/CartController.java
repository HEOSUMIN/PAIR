package com.pro.pair.cart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pro.pair.cart.model.dao.CartMapper;
import com.pro.pair.cart.model.dto.CartDTO;
import com.pro.pair.cart.model.service.CartService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CartController {

	private final CartService cartService;
	private CartMapper cartMapper;

	@Autowired
	public CartController(CartService cartService, CartMapper cartMapper) {
		this.cartService = cartService;
		this.cartMapper = cartMapper;
	}

	@GetMapping("/cart/mycart")
	public void getMycart(HttpSession session, Model model) {
		String loginMember = (String) session.getAttribute("loginMember");

		if (loginMember != null) {
			List<CartDTO> memberCart = cartService.getCartList(loginMember);
			model.addAttribute("loginMember", loginMember);
			model.addAttribute("memberCart", memberCart);
		}
	}

	/*
	 * 장바구니 담기
	 */
	@PostMapping(value = "/cart/mycart/add", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String addToCart(@RequestBody List<CartDTO> optionArr, Principal principal, HttpSession session,
			Model model) {
		String loginMember = (String) session.getAttribute("loginMember");
		log.info("optionArr: {}", optionArr);

		String result = "";

		// 로그인 필수
		if (loginMember == null) {
			log.info("로그인해주세요 ");
			return "LOGIN_REQUIRED";
		}

		for (CartDTO dto : optionArr) {
			dto.setMemberId(loginMember);

			// 이미 장바구니에 있는 옵션인지 확인
			CartDTO existCart = cartMapper.selectCartByOption(loginMember, dto.getProdNo(), dto.getOptCombNo());
			log.info("existCart: {}", existCart);
			if (existCart != null) {
				int sum = existCart.getQuantity() + dto.getQuantity();
				cartMapper.updateQuantity(loginMember, dto.getOptCombNo(), sum);
				result = "이미 장바구니에 담긴 상품입니다";
			} else {
				cartMapper.insertCart(dto);
			}
		}
		List<CartDTO> memberCart = cartMapper.getCartList(loginMember);
		session.setAttribute("countCartItem", memberCart.size());

		if (result.isEmpty()) {
			result = "성공";
		}
		return result;
	}

	/*
	 * 장바구니 수량 변경
	 */
	@PostMapping("/cart/mycart/modify")
	@ResponseBody
	public void changeQuantity(@RequestBody Map<String, String> param, HttpSession session) {
		String quantity = param.get("quantity");
		String optCombNo = param.get("optCombNo");

		log.info("변경 수량 : {}", quantity);
		log.info("해당 옵션 : {}", optCombNo);

		String loginMember = (String) session.getAttribute("loginMember");

		/* 장바구니 호출 */
		List<CartDTO> memberCart = cartMapper.getCartList(loginMember);
		for (int i = 0; i < memberCart.size(); i++) {
			log.info("!!!!!!!!: {}", memberCart.get(i).getOptCombNo());
			if (Integer.parseInt(optCombNo) == memberCart.get(i).getOptCombNo()) {
				log.info("해당 loginMember : {}", loginMember);
				log.info("!???: {}", memberCart.get(i).getOptCombNo());
				cartMapper.updateQuantity(loginMember, Integer.parseInt(optCombNo), Integer.parseInt(quantity));
			} else {
				continue;
			}
		}
		session.setAttribute("memberCart", memberCart);

	}

	/*
	 * 장바구니 행 삭제
	 */
	@PostMapping(value = "cart/mycart/delete", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public void deleteCartItem(@RequestBody Map<String, String> param, HttpSession session) {
		String optCombNo = param.get("optCombNo");
		log.info("삭제 요청 옵션 : {}", optCombNo);

		String loginMember = (String) session.getAttribute("loginMember");

		List<CartDTO> memberCart = cartMapper.getCartList(loginMember);
		log.info("회원용 장바구니 호출");
		for (int j = 0; j < memberCart.size(); j++) {
			if (Integer.parseInt(optCombNo) == memberCart.get(j).getOptCombNo()) {
				cartMapper.deleteCartItem(loginMember, Integer.parseInt(optCombNo));
			}
		}

		memberCart = cartMapper.getCartList(loginMember);
		session.setAttribute("countCartItem", memberCart.size());
	}

	/*
	 * 장바구니 선택 삭제
	 */
	@PostMapping(value = "cart/mycart/deleteCheck", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public void deleteCheckCartItem(@RequestBody List<Integer> optCombNo, HttpSession session) {
		String loginMember = (String) session.getAttribute("loginMember");

		cartMapper.deleteCartItems(loginMember, optCombNo);

		List<CartDTO> memberCart = cartMapper.getCartList(loginMember);
		log.info("선택 상품 삭제 완료된 회원용 장바구니 : {}", memberCart);
		session.setAttribute("countCartItem", memberCart.size());
	}
}
