package com.example.petify.mapper.ecom;

import com.example.petify.dto.ecom.OrderDto;
import com.example.petify.model.order.Order;

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
