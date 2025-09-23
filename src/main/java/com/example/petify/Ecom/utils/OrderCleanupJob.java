package com.example.petify.Ecom.utils;

import com.example.petify.domain.order.model.OrderStatus;
import com.example.petify.domain.order.repository.OrderRepository;
import com.example.petify.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderCleanupJob {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    public OrderCleanupJob(OrderRepository orderRepo, ProductRepository productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // runs every minute
    public void releaseExpiredOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);

            var expiredOrders = orderRepo.findByStatusAndCreatedAtBefore(
                OrderStatus.PENDING_PAYMENT, cutoff);

        expiredOrders.forEach(order -> {
            // Restore stock
            order.getOrderProducts().forEach(op -> {
                try {
                    op.getProduct().releaseReservedStock(op.getQuantity());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                productRepo.save(op.getProduct());
            });

            order.setStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);
        });
    }
}