package com.example.petify.service.ecom.impl;

import com.example.petify.dto.ecom.CheckoutDto;
import com.example.petify.dto.ecom.CheckoutResponse;
import com.example.petify.dto.ecom.PaymentDto;
import com.example.petify.mapper.ecom.OrderMapper;
import com.example.petify.service.ecom.CheckoutService;
import com.example.petify.service.ecom.PaymobService;
import com.example.petify.config.AppConfig;
import com.example.petify.model.cart.Cart;
import com.example.petify.model.cart.CartProduct;
import com.example.petify.repository.cart.CartRepository;
import com.example.petify.model.order.Order;
import com.example.petify.model.order.OrderProduct;
import com.example.petify.model.order.OrderStatus;
import com.example.petify.repository.order.OrderRepository;
import com.example.petify.repository.product.ProductRepository;
import com.example.petify.model.profile.POProfile;
import com.example.petify.model.user.User;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

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
