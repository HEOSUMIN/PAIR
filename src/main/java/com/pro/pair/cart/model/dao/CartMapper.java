package com.pro.pair.cart.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.cart.model.dto.CartDTO;

@Mapper
public interface CartMapper {

	List<CartDTO> getCartList(String memberId);

	CartDTO selectCartByOption(String memberId, int prodNo, int optCombNo);

	int updateQuantity(String memberId, int prodNo, int optCombNo, int quantity);

	int insertCart(CartDTO cart);
}
