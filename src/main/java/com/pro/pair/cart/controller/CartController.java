package com.pro.pair.cart.controller;

import java.security.Principal;
import java.util.List;

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
		
		if(loginMember != null) {
			List<CartDTO> memberCart = cartService.getCartList(loginMember);
			model.addAttribute("loginMember", loginMember);
			model.addAttribute("memberCart", memberCart);
		}
	}
	
	/*
	 * 장바구니
	 */
	@PostMapping(value="/cart/mycart/add", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String addToCart(@RequestBody List<CartDTO> optionArr,Principal principal, HttpSession session, Model model) {
		String loginMember = (String) session.getAttribute("loginMember");
		log.info("optionArr: {}", optionArr);
		
		String result = "";
		
		//로그인 필수 
		if (loginMember == null) {
			log.info("로그인해주세요 ");
		    return "LOGIN_REQUIRED";
		}
		
		for(CartDTO dto : optionArr) {
			dto.setMemberId(loginMember);

	        // 이미 장바구니에 있는 옵션인지 확인
	        CartDTO existCart = cartMapper.selectCartByOption(loginMember, dto.getProdNo(), dto.getOptCombNo());
	        log.info("existCart: {}", existCart);
	        if (existCart != null) {
	            int sum = existCart.getQuantity() + dto.getQuantity();
	            cartMapper.updateQuantity(loginMember, dto.getProdNo(), dto.getOptCombNo(), sum);
	            result = "이미 장바구니에 담긴 상품입니다";
	        } else {
	            cartMapper.insertCart(dto);
	        }
		}
		
		if(result.isEmpty()) {
			result = "성공";
		}
		return result;
	}
}
