package com.pro.pair.cart.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.cart.model.dto.DeliveryDTO;
import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.dto.OrderItemDTO;
import com.pro.pair.cart.model.dto.PaymentDTO;

@Mapper
public interface OrderMapper {

	int addOrder(OrderDTO orderDTO);
	
	int addOrderItem(OrderItemDTO orderItem);
	
	int decreaseStockAmount(int optCombNo, int orderQuantity);
	
	int addDeliveryInfo(DeliveryDTO deliveryDTO);
	
	int addPaymentInfo(PaymentDTO paymanetDTO);
}
