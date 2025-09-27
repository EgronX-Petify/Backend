package com.example.petify.Ecom.dto;

import com.example.petify.domain.order.model.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDto {

    private Long orderId;
    private Long buyerId;
    private String contactInfo;
    private  String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private List<OrderProductDto> items;

    private Double fees;
    private Double totalPrice;

    @Data
    @Builder
    public static class OrderProductDto {
        private Long productId;
        private Long quantity;
    }

    public void addItem(Long productId, Long quantity) {
        this.items.add(OrderProductDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build());
    }
}
