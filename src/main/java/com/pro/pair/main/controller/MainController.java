package com.pro.pair.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.product.model.service.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	private final ProductService productService;
	
	public MainController(ProductService productService) {
		this.productService = productService;
	}
	

	@GetMapping(value={"/", "/main"})
	public String pairMain(HttpSession session, Model model) {
		
		//조합 2개 이상인 상품 목록 3개 추출
		List<ProductDTO> recommendProdList =  productService.getTodayRecommendation(3);
		
		log.info("recommendProdList: {}", recommendProdList);
		
		model.addAttribute("recommendProdList", recommendProdList);
		return "main";
	}
}
