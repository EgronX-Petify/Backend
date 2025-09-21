package com.example.petify.Ecom.dto;

import com.example.petify.domain.order.model.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderStatusRequest {
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
