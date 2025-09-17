package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.OrderDto;
import com.example.petify.Ecom.dto.OrderFilter;
import com.example.petify.domain.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    public OrderDto getOrderById(Long id);
    public Page<OrderDto> getOrders(OrderFilter orderFilter, Pageable pageable);
    public OrderDto UpdateOrderStatus(Long id, OrderStatus orderStatus);

}
