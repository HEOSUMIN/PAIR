package com.pro.pair.cart.model.service;

import java.util.List;

import com.pro.pair.cart.model.dto.CartDTO;

public interface CartService {
	
	List<CartDTO> getCartList(String memberId);
	
}
