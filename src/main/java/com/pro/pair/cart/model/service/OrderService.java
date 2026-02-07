package com.pro.pair.cart.model.service;

import java.util.List;

import com.pro.pair.cart.model.dto.DeliveryDTO;
import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.dto.OrderItemDTO;
import com.pro.pair.cart.model.dto.PaymentDTO;

public interface OrderService {
	
	boolean orderAndPay(OrderDTO orderDTO, List<OrderItemDTO> items, DeliveryDTO deliveryDTO, PaymentDTO paymentDTO);
	

}
