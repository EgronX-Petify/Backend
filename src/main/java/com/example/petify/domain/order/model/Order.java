package com.example.petify.domain.order.model;

import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long paymobTrnxOrderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String address;

    @Column
    @Builder.Default
    private double fees = 0;

    @Column(name = "total_price" , nullable = false)
    private double totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;


    public void addOrderProduct(OrderProduct orderProduct){
        this.orderProducts.add(orderProduct);
    }


}
