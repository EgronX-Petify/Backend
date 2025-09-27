package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.CheckoutDto;
import com.example.petify.Ecom.dto.CheckoutResponse;
import com.example.petify.Ecom.dto.PaymentDto;
import com.example.petify.Ecom.mapper.OrderMapper;
import com.example.petify.Ecom.services.CheckoutService;
import com.example.petify.Ecom.services.PaymobService;
import com.example.petify.config.AppConfig;
import com.example.petify.domain.cart.model.Cart;
import com.example.petify.domain.cart.model.CartProduct;
import com.example.petify.domain.cart.repository.CartRepository;
import com.example.petify.domain.order.model.Order;
import com.example.petify.domain.order.model.OrderProduct;
import com.example.petify.domain.order.model.OrderStatus;
import com.example.petify.domain.order.repository.OrderRepository;
import com.example.petify.domain.product.repository.ProductRepository;
import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final AppConfig appConfig;
    private final PaymobService paymobService;

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public CheckoutResponse Checkout(Long userId, CheckoutDto data) {
        // get user
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("No user found with id: " + userId)
        );

        // get cart
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No cart found with user id: " + userId)
        );

        // create order
        Order order = Order.builder()
                .user(user)
                .address(data.getAddress())
                .contactInfo(data.getContactInfo())
                .notes(data.getNotes())
                .build();

        // add order products
        double totalPrice = 0;
        for (CartProduct cartProduct : cart.getCartProducts()) {
            var product = cartProduct.getProduct();
            try {
                product.reserveStock(cartProduct.getQuantity());
                productRepository.save(product);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            totalPrice += product.getPrice() * (1 - product.getDiscount() / 100);
            order.addOrderProduct(OrderProduct.builder()
                    .product(product)
                    .quantity(cartProduct.getQuantity())
                    .priceAtOrder(product.getPrice())
                    .build());
        }
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalPrice(totalPrice);

        // generate payment link
        var json = paymobService.generateIntentionPayment(
                PaymentDto.builder()
                        .amountInCents((float)totalPrice * 100)
                        .paymentMethods(List.of(Integer.parseInt(appConfig.getPaymobIntegrationId())))
                        .billingData(PaymentDto.BillingData.builder()
                                .email(user.getEmail())
                                .firstName(((POProfile) user.getProfile()).getName())
                                .lastName("--")
                                .phoneNumber(user.getProfile().getPhoneNumber())
                                .build())
                        .notificationUrl(appConfig.getWebhookUrl())
                        .build()
        );

        String clientSecretKey =json.get("client_secret").toString();
        String paymentUrl = "https://accept.paymob.com/unifiedcheckout/?publicKey="+appConfig.getPaymobPublicKey() + "&clientSecret=" + clientSecretKey;
        String trnx_orderId = json.get("intention_order_id").toString();

        order.setPaymobTrnxOrderId(Long.parseLong(trnx_orderId));
        order = orderRepository.save(order);

        //
        cart.setCartProducts(new HashSet<>());
        cartRepository.save(cart);

        return CheckoutResponse.builder()
                .paymentUrl(paymentUrl)
                .order(OrderMapper.toDto(order))
                .build();
    }
}
