package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.OrderDto;
import com.example.petify.Ecom.dto.OrderFilter;
import com.example.petify.Ecom.mapper.OrderMapper;
import com.example.petify.Ecom.services.OrderService;
import com.example.petify.Ecom.services.PaymobService;
import com.example.petify.domain.order.model.Order;
import com.example.petify.domain.order.model.OrderStatus;
import com.example.petify.domain.order.repository.OrderRepository;
import com.example.petify.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Data
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;
    private final PaymobService paymobService;

    @Override
    public OrderDto getOrderById(Long id) {

        Order order = orderRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order with id: " + id + " not found")
        );
        return OrderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> getOrders(OrderFilter orderFilter, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto UpdateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order with id: " + id + " not found")
        );
        order.setStatus(orderStatus);
        if (orderStatus.equals(OrderStatus.CANCELLED)) {
            double amount_in_cents = order.getTotalPrice() * 100;
//            paymobService.refund(order.getPaymobTrnxOrderId().toString(), (int) amount_in_cents);
            // TODO: refund
        }
        orderRepo.save(order);
        return OrderMapper.toDto(order);
    }
}
