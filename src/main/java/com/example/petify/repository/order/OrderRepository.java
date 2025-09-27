package com.example.petify.repository.order;

import com.example.petify.model.order.Order;
import com.example.petify.model.order.OrderStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findAll(Specification<Order> spec, @NotNull Pageable pageable);

    Optional<Order> findByPaymobTrnxOrderId(Long trnxOrderId);
    List<Order> findByStatusAndCreatedAtBefore(
            OrderStatus status, LocalDateTime createdAt
    );
}
