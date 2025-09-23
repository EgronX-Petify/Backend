package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.services.PaymobService;
import com.example.petify.Ecom.services.PaymobWebhook;
import com.example.petify.domain.order.model.Order;
import com.example.petify.domain.order.model.OrderStatus;
import com.example.petify.domain.order.model.PaymentTransaction;
import com.example.petify.domain.order.repository.OrderRepository;
import com.example.petify.domain.order.repository.PaymentTransactionRepository;
import com.example.petify.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class PaymobWebhookImpl implements PaymobWebhook {
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final PaymentTransactionRepository trnxRepo;
    private final PaymobService paymobService;

    @Override
    @Transactional
    public void handleTransaction(JsonNode payload) {
        String trnx_id = payload.get("obj.id").asText();
        String trnx_orderId = payload.get("obj.order.id").asText();

        String paymentStatus = payload.get("obj.order.payment_status").asText();

        String trnx_currency = payload.get("obj.currency").asText();
        Integer trnx_amountInCents = payload.get("obj.amount_cents").asInt();

        Boolean trnx_isCapture = payload.get("obj.is_capture").asBoolean();
        Boolean trnx_isVoided = payload.get("obj.is_voided").asBoolean();
        Boolean trnx_isRefund = payload.get("obj.is_refunded").asBoolean();

        boolean trnx_isSuccess = payload.get("obj.success").asBoolean();

        Order order = orderRepo.findByPaymobTrnxOrderId(
                Long.parseLong(trnx_orderId))
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        PaymentTransaction trnx = new PaymentTransaction();

        trnx.setTransactionId(trnx_id);
        trnx.setTransactionOrderId(trnx_orderId);
        trnx.setCurrency(trnx_currency);
        trnx.setAmountInCents(trnx_amountInCents);
        trnx.setStatus(paymentStatus);

        trnx.setIsCapture(trnx_isCapture);
        trnx.setIsVoided(trnx_isVoided);
        trnx.setIsRefunded(trnx_isRefund);

        trnx.setCreatedAt(LocalDateTime.now());
        trnx.setUpdatedAt(LocalDateTime.now());
        trnxRepo.save(trnx);

        if (trnx_isSuccess) {
            order.getOrderProducts().forEach(orderProduct -> {
               var product = orderProduct.getProduct();
                try {
                    product.confirmSale(orderProduct.getQuantity());
                    productRepo.save(product);
                } catch (Exception e) {
                    paymobService.refund(trnx_id, trnx_amountInCents);
                    order.setStatus(OrderStatus.REFUNDED);
                    throw new RuntimeException(e);
                }
            });

            order.setStatus(OrderStatus.PAID);
            orderRepo.save(order);
        }

    }
}
