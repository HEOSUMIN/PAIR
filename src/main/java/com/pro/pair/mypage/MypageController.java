package com.pro.pair.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.service.OrderService;
import com.pro.pair.member.model.dto.UserImpl;
import com.pro.pair.member.model.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller()
@RequestMapping("/mypage")
public class MypageController {
	
	private MemberService memberService;
	private OrderService orderService;
	
	@Autowired
	public MypageController(MemberService memberService, OrderService orderService) {
		this.memberService = memberService;
		this.orderService = orderService;
	}

	/*
	 * 마이페이지 메인 화면
	 */
	@GetMapping("/main")
	public void getMypage(@AuthenticationPrincipal UserImpl user, HttpSession session, Model model) {
		model.addAttribute("preparingOrderCount", 0); //최근 3개월 주문내역(상품준비중)
		model.addAttribute("dispatchedOrderCount", 0);
		model.addAttribute("deliveredOrderCount", 0);
	}
	
	/*
	 * 마이페이지 주문/배송 목록
	 */
	@GetMapping("/order")
	public void getOrderList(@AuthenticationPrincipal UserImpl user, Model model) {
		List<OrderDTO> memberOrderList = memberService.getMemberOrderList(user.getUsername());
		Map<String, Integer> numberOfEachOrder = new HashMap<>();
		String orderNo = "";
		int number = 0;
		for(int i=0; i < memberOrderList.size(); i++) {
			orderNo = memberOrderList.get(i).getOrderItem().getOrderNo();
			number = orderService.getTheNumberOfEachOrder(orderNo);
			numberOfEachOrder.put(orderNo, number);
		}
		model.addAttribute("memberPoint", 0); //현재 보유 적립금
		model.addAttribute("dispatchedOrderCount", memberService.getMemberOrderCountByDlvrStatus(user.getMemberId(), "배송중")); //최근 3개월 주문내역(배송중)
		model.addAttribute("numberOfEachOrder", numberOfEachOrder);
		model.addAttribute("memberOrderList", memberOrderList);
	}
}
