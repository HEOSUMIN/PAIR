package com.pro.pair.cart.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.cart.model.dto.CartDTO;

@Mapper
public interface CartMapper {

	List<CartDTO> getCartList(String memberId);

	CartDTO selectCartByOption(String memberId, int prodNo, int optCombNo);
	
	void insertCart(CartDTO cart);

	void updateQuantity(String memberId, int optCombNo, int quantity);
	
	void deleteCartItem(String memberId, int optCombNo);
	
	void deleteCartItems(String memberId, List<Integer> optCombNo);

	
}
