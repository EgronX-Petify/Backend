package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.OrderDto;
import com.example.petify.dto.ecom.OrderFilter;
import com.example.petify.model.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    public OrderDto getOrderById(Long id);
    public Page<OrderDto> getOrders(OrderFilter orderFilter, Pageable pageable);
    public OrderDto UpdateOrderStatus(Long id, OrderStatus orderStatus);

}
