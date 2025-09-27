package com.example.petify.dto.ecom;

import com.example.petify.model.order.OrderStatus;
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
