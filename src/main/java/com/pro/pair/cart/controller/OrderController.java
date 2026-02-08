package com.pro.pair.cart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pro.pair.cart.model.dto.CartDTO;
import com.pro.pair.cart.model.dto.DeliveryDTO;
import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.dto.OrderItemDTO;
import com.pro.pair.cart.model.dto.PaymentDTO;
import com.pro.pair.cart.model.service.CartService;
import com.pro.pair.cart.model.service.OrderService;
import com.pro.pair.member.model.dto.UserImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {
	
	private CartService cartService;
	private OrderService orderService;
	
	
	@Autowired
	public OrderController(CartService cartService, OrderService orderService) {
		this.cartService = cartService;
		this.orderService = orderService;
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
		if (optArr != null && optArr.length > 0) {
		    List<CartDTO> orderItemList = new ArrayList<>();
		    int totalPrice = 0;
		    
		    for (String optCombNoStr : optArr) {
		        int optCombNo = Integer.parseInt(optCombNoStr);
		        CartDTO cartItem = cartService.getCartItemByOptionNo(user.getMemberId(), optCombNo);
		       
		        if (cartItem != null) {
		        	int price = cartItem.getProduct().getPrice();
			        int optAddPrice = cartItem.getOptionComb().getOptAddPrice();
			        int discountRate = cartItem.getProduct().getDiscountRate();
			        int quantity = cartItem.getQuantity();

			        int itemTotal = (price - (price * discountRate / 100) + optAddPrice) * quantity;
			        totalPrice += itemTotal;
			        log.info("totalPrice:{}", totalPrice);
			        
		            orderItemList.add(cartItem);
		        }
		    }
		     log.info("총 totalPrice:{}", totalPrice);
		     
		    session.setAttribute("totalPrice", totalPrice);
		    session.setAttribute("orderItem", orderItemList);
		}
		
		/* 장바구니 선택 or 전체 주문 */
		model.addAttribute("member", user);
		log.info("user:{}", user);
		
		return "/cart/order";
	}
	
	/*
	 * 주문 및 결제
	 */
	@PostMapping(value="/cart/order", produces="application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> orderAndPay(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session) {
		log.info("Server-side 주문 및 결제 시작");
		Map<String, Object> result = new HashMap<>();
		
		/* 주문 품목 저장 */
	    List<OrderItemDTO> items = ((List<Map<String, Object>>) params.get("items"))
	        .stream()
	        .map(m -> {
	            OrderItemDTO dto = new OrderItemDTO();
	            dto.setOptCombNo((Integer) m.get("optCombNo"));
	            dto.setOrderQuantity((Integer) m.get("quantity"));
	            dto.setOrderAmount((Integer) m.get("price"));
	            return dto;
	        })
	        .collect(Collectors.toList());
		
	    String memberId = (String) params.get("memberId");
	    log.info("memberId: ", memberId);
	    String rcvrName = params.get("rcvrName").toString();
	    log.info("rcvrName : {}", rcvrName);
	    String rcvrPhone = params.get("rcvrPhone").toString();
		String rcvrAddress = params.get("rcvrAddress").toString();
		String dlvrReqMessage = params.get("dlvrReqMessage").toString();
		String deliveryFee = params.get("deliveryFee").toString();
		//String pointAmount = params.get("pointAmount").toString();
		String paymentMethod = params.get("paymentMethod").toString();
		String paymentAmount = params.get("paymentAmount").toString();
		
		/* 주문 정보 등록 */
		OrderDTO OrderDTO = new OrderDTO();
		OrderDTO.setMemberId(memberId);
		OrderDTO.setRcvrName(rcvrName);
		OrderDTO.setRcvrPhone(rcvrPhone);
		OrderDTO.setRcvrAddress(rcvrAddress);
		OrderDTO.setDlvrReqMsg(dlvrReqMessage);
		OrderDTO.setDlvrStatus("상품준비중");
		
		/* 배송 정보 등록 */
		DeliveryDTO deliveryDTO = new DeliveryDTO();
		deliveryDTO.setDeliveryFee(Integer.parseInt(deliveryFee));
		deliveryDTO.setDeliveryCompany("업체배송");
		
		/* 결제 정보 등록 */
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setPaymentMethod(paymentMethod);
		paymentDTO.setPaymentAmount(Integer.parseInt(paymentAmount));
		
		/* 적립금 등록 - 추후수정 */
		
		boolean isCommited = orderService.orderAndPay(OrderDTO, items, deliveryDTO, paymentDTO );

		if (isCommited) {
	        result.put("result", "succeed");
	        result.put("orderNo", OrderDTO.getOrderNo());
	    } else {
	        result.put("result", "failed");
	    }
		log.info("result : {}", result);
		return result;
	}
}













