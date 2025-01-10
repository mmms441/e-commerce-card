package com.wisely.e_commercecard.service.order;

import com.wisely.e_commercecard.dto.OrderDto;
import com.wisely.e_commercecard.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder (Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
