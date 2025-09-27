package com.example.petify.dto.ecom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilter {
    private Long profileId;
    private Long productId;
    private Long sellerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
