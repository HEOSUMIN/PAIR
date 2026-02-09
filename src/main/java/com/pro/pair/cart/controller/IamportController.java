package com.pro.pair.cart.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/verifyiamport")
public class IamportController {
	private final IamportClient iamportClient;
	private static final String API_KEY = "3463738332864466";
	private static final String API_SECRET = "KhQaD4x7TSjUz3Fz0JbjpwvUJ3nks2fJTlZabIgxf1KrViHW69TJKYMaH0hZQMpSuqzg8g8tDsC8gmvy";
	

	public IamportController() {
		this.iamportClient = new IamportClient(API_KEY, API_SECRET);
	}
	
	@PostMapping("/{imp_uid}")
	@ResponseBody
	public IamportResponse<Payment> paymentByImpUid(@PathVariable(value="imp_uid") String imp_uid) throws IamportResponseException, IOException {
		log.info("paymentByImpUid:{}" , imp_uid);
		return iamportClient.paymentByImpUid(imp_uid);
	}
	
//	
//	@PostMapping("/{imp_uid}")
//	@ResponseBody
//	public Map<String, Object> verifyTest(@PathVariable String imp_uid) {
//	    // 테스트 결제라고 가정
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("success", true);
//	    response.put("amount", 1000); // 테스트 금액
//	    response.put("imp_uid", imp_uid);
//	    return response;
//	}
//	
}
