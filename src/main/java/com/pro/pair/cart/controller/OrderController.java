package com.pro.pair.cart.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pro.pair.cart.model.dto.CartDTO;
import com.pro.pair.cart.model.service.CartService;
import com.pro.pair.member.model.dto.UserImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {
	
	private CartService cartService;
	
	@Autowired
	public OrderController(CartService cartService) {
		this.cartService = cartService;
	}
	
	/*
	 * 주문결제페이지 호출
	 */
	@GetMapping("/cart/order")
	public String getOrderForm(@AuthenticationPrincipal UserImpl user, HttpServletRequest request, HttpSession session, Model model) {
		//장바구니 선택상품 또는 전체상품
		String[] optArr = request.getParameterValues("arr");
		
		List<CartDTO> memberCart = cartService.getCartList(user.getMemberId());
		
		
		/* 상세페이지 바로 주문 */
		
		
		/* 장바구니 단일 상품 주문 */
		if(optArr != null) {
			List<CartDTO> itemList = new ArrayList<>();
			for(int i=0; i < memberCart.size(); i++) {
				for(int j=0; j < optArr.length; j++) {
					CartDTO cartDTO = new CartDTO();
					if(memberCart.get(i).getOptCombNo() == Integer.parseInt(optArr[j])) {
						cartDTO = cartService.getCartItemByOptionNo(user.getMemberId(), Integer.parseInt(optArr[j]));
						log.info("item : {}", cartDTO);
						itemList.add(cartDTO);
					} else {
						continue;
					}
				}
			}
			session.setAttribute("orderItem", itemList); 
		}
		
		/* 장바구니 선택 or 전체 주문 */
		model.addAttribute("member", user);
		
		return "/cart/order";
	}

}
