package com.pro.pair.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class productController {
	
	
	/* 관리자 - 상품등록 */
	@GetMapping("/admin/product/add")
	public void addProduct(Model model) {
	//여기 등록페이지 나오는거 부터 수정하
		
	}

}
