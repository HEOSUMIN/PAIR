package com.pro.pair.cart.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pro.pair.cart.model.dao.OrderMapper;
import com.pro.pair.cart.model.dto.DeliveryDTO;
import com.pro.pair.cart.model.dto.OrderDTO;
import com.pro.pair.cart.model.dto.OrderItemDTO;
import com.pro.pair.cart.model.dto.PaymentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private OrderMapper orderMapper;
	
	@Autowired
	public OrderServiceImpl(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Transactional(rollbackFor={Exception.class, Error.class})
	@Override
	public boolean orderAndPay(OrderDTO orderDTO, List<OrderItemDTO> items, DeliveryDTO deliveryDTO,
			PaymentDTO paymentDTO) {
		boolean result = false;
		
		//주문정보 insert 
		int addOrderDetail = orderMapper.addOrder(orderDTO);
		 
		int countAdd = 0;
		int countDecrease = 0;
		for (OrderItemDTO item : items) {
			log.info("getOrderNo: {}   ",orderDTO.getOrderNo());
		    item.setOrderNo(orderDTO.getOrderNo()); // 주문번호 세팅
		    
		    // 주문 품목 insert
		    countAdd += orderMapper.addOrderItem(item);
		    
		    //주문 품목 재고 차감 
		    countDecrease += orderMapper.decreaseStockAmount(item.getOptCombNo(), item.getOrderQuantity());
		}
		
		//배송정보 insert
		deliveryDTO.setOrderNo(orderDTO.getOrderNo());
		int addDeliveryInfo = orderMapper.addDeliveryInfo(deliveryDTO);
		
		//결제정보 insert
		paymentDTO.setOrderNo(orderDTO.getOrderNo());
		int addPaymentInfo = orderMapper.addPaymentInfo(paymentDTO);
		
		log.info("addOrderDetail: {} ",addOrderDetail);
		log.info("countAdd: {}  ",countAdd);
		log.info("countDecrease: {}  ",countDecrease);
		log.info("addDeliveryInfo: {}  ",addDeliveryInfo);
		log.info("addPaymentInfo: {}  ",addPaymentInfo);
		
		if(addOrderDetail == 1 && countAdd == items.size() && countDecrease == items.size()
				&& addDeliveryInfo == 1 && addPaymentInfo ==1) {
			result = true;
		}
		return result;
	}
}
