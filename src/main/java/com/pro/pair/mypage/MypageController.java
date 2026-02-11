package com.pro.pair.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.service.OrderService;
import com.pro.pair.member.model.dto.UserImpl;
import com.pro.pair.member.model.service.MemberService;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.product.model.service.ProductService;
import com.pro.pair.review.model.dto.ReviewDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller()
@RequestMapping("/mypage")
public class MypageController {
	
	private MemberService memberService;
	private OrderService orderService;
	private ProductService productService;
	
	@Autowired
	public MypageController(MemberService memberService, OrderService orderService, ProductService productService) {
		this.memberService = memberService;
		this.orderService = orderService;
		this.productService = productService;
	}

	/*
	 * 마이페이지 메인 화면
	 */
	@GetMapping("/main")
	public void getMypage(@AuthenticationPrincipal UserImpl user, HttpSession session, Model model) {
		log.info("id;{}",user.getMemberId());
		List<Integer> recentlyViewed = (List<Integer>) session.getAttribute("recentlyViewed");
		
		int writableRevwCnt = memberService.getWritableReviewCount(user.getMemberId());
		
		if(recentlyViewed != null) {
			List<ProductDTO> recentlyViewedItems = new ArrayList<>();
			List<AttachmentDTO> recentlyViewedThumbnailList = new ArrayList<>();
			
			for(int i=0; i < recentlyViewed.size(); i++) { //출력용 상품 이름, 메인썸네일
				int prodNo = recentlyViewed.get(i);
				ProductDTO productDTO = productService.getProductDetails(prodNo);
				recentlyViewedItems.add(productDTO);
				AttachmentDTO mainThumb = productService.getMainThumbnailByProdNo(prodNo);
				recentlyViewedThumbnailList.add(mainThumb);
			}
			model.addAttribute("recentlyViewedItems", recentlyViewedItems); //최근 본 상품
			model.addAttribute("recentlyViewedThumbnailList", recentlyViewedThumbnailList);
		}
		
		
		model.addAttribute("writableRevwCnt", writableRevwCnt);		//작성 가능한 리뷰 개수 
		model.addAttribute("preparingOrderCount", memberService.getMemberOrderCountByDlvrStatus(user.getMemberId(), "상품준비중")); //최근 3개월 주문내역(상품준비중)
		model.addAttribute("dispatchedOrderCount", memberService.getMemberOrderCountByDlvrStatus(user.getMemberId(), "배송중"));
		model.addAttribute("deliveredOrderCount", memberService.getMemberOrderCountByDlvrStatus(user.getMemberId(), "배송완료"));
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
	
	/*
	 * 마이페이지 주문/배송 상세 조회
	 */
	@GetMapping("/order/details")
	public void getOrderListDetails(@RequestParam("no") String orderNo, @AuthenticationPrincipal UserImpl user, Model model) {
		log.info("상세조회 요청 주문번호 : {}", orderNo);
		OrderDTO memberOrderDetails = memberService.getMemberOrderDetails(user.getMemberId(), orderNo);
		String method = memberOrderDetails.getPayment().getPaymentMethod();
		switch(method) {
		case "card": method = "신용카드"; break;
		case "trans": method = "실시간계좌이체"; break;
		case "vbank": method = "가상계좌"; break;
		case "phone": method = "휴대폰결제"; break;
		}
		memberOrderDetails.getPayment().setPaymentMethod(method);
		
		List<OrderDTO> orderOptionList = memberService.getOptionListByOrderNo(orderNo);
		
		int totalOrderAmount = memberService.getTotalOrderAmountByOrderNo(orderNo);
		
		model.addAttribute("memberOrderDetails", memberOrderDetails);
		model.addAttribute("orderOptionList", orderOptionList);
		model.addAttribute("totalOrderAmount", totalOrderAmount);
		
	}
	
	/*
	 * 상품리뷰
	 */
	@GetMapping("/review")
	public void getReviewList(@AuthenticationPrincipal UserImpl user, HttpSession session, Model model) {
		/* 작성 가능한 리뷰 */
		List<OrderDTO> itemList = memberService.getItemsToPostAReview(user.getMemberId());
	
		/* 작성한 리뷰 */
		List<ReviewDTO> postList = memberService.getMemberReviewPosts(user.getMemberId());
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("postList", postList);
	}
}









