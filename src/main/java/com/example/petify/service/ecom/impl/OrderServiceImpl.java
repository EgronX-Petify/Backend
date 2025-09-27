package com.example.petify.service.ecom.impl;

import com.example.petify.dto.ecom.OrderDto;
import com.example.petify.dto.ecom.OrderFilter;
import com.example.petify.mapper.ecom.OrderMapper;
import com.example.petify.service.ecom.OrderService;
import com.example.petify.service.ecom.PaymobService;
import com.example.petify.specfication.OrderSpecification;
import com.example.petify.model.order.Order;
import com.example.petify.model.order.OrderStatus;
import com.example.petify.repository.order.OrderRepository;
import com.example.petify.exception.ResourceNotFoundException;
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
        return orderRepo.findAll(OrderSpecification.build(orderFilter), pageable).map(OrderMapper::toDto);
    }

    @Override
    public OrderDto UpdateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order with id: " + id + " not found")
        );
        if (orderStatus.equals(OrderStatus.CANCELLED)) {
            double amount_in_cents = order.getTotalPrice() * 100;
            String trnxId = paymobService.getTransactionByTrnxOrderId(
                    order.getPaymobTrnxOrderId()).get("id").toString();
            paymobService.refund(trnxId, (int) amount_in_cents);
        }
        order.setStatus(orderStatus);
        orderRepo.save(order);
        return OrderMapper.toDto(order);
    }
}
