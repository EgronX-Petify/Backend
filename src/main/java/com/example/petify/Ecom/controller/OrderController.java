package com.example.petify.Ecom.controller;


import com.example.petify.Ecom.dto.OrderDto;
import com.example.petify.Ecom.dto.OrderFilter;
import com.example.petify.Ecom.dto.UpdateOrderStatusRequest;
import com.example.petify.Ecom.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrders(
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        OrderFilter filter = new OrderFilter();
        filter.setProfileId(buyerId);
        filter.setProductId(productId);
        filter.setSellerId(sellerId);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(
                orderService.getOrders(filter, pageRequest)
        );
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            UpdateOrderStatusRequest req
    ) {
        return ResponseEntity.ok(
                orderService.UpdateOrderStatus(req.getId(), req.getStatus())
        );
    }
}
