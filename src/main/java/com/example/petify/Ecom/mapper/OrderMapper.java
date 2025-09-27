package com.example.petify.Ecom.mapper;

import com.example.petify.Ecom.dto.OrderDto;
import com.example.petify.domain.order.model.Order;

public class OrderMapper {
    public static OrderDto toDto(Order order) {
        OrderDto orderDto = OrderDto.builder()
                .orderId(order.getId())
                .fees(order.getFees())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .buyerId(order.getUser().getId())
                .contactInfo(order.getContactInfo())
                .address(order.getAddress())
                .build();
        for (var item : order.getOrderProducts()) {
            orderDto.addItem(item.getProduct().getId(), (long) item.getQuantity());
        }
        return orderDto;
    }
}
