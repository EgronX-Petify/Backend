package com.example.petify.specfication;

import com.example.petify.dto.ecom.OrderFilter;
import com.example.petify.model.order.Order;
import com.example.petify.model.order.OrderProduct;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> containsProduct(Long productId) {
        return (root, query, cb) -> {
            if (productId == null) return cb.conjunction(); // no filter
            // join orderProducts -> product
            Join<Order, OrderProduct> orderProductJoin = root.join("orderProducts", JoinType.INNER);
            return cb.equal(orderProductJoin.get("product").get("id"), productId);
        };
    }

    public static Specification<Order> hasSeller(Long sellerId) {
        return (root, query, cb) -> {
            if (sellerId == null) return cb.conjunction();
            // assuming OrderProduct has a Product, and Product has a Seller
            Join<Order, OrderProduct> orderProductJoin = root.join("orderProducts", JoinType.INNER);
            return cb.equal(orderProductJoin.get("product").get("seller").get("id"), sellerId);
        };
    }

    public static Specification<Order> hasBayer(Long poProfileId) {
        return (root, query, cb) -> {
            if (poProfileId == null) return cb.conjunction();
            return cb.equal(root.get("profile").get("id"), poProfileId);
        };
    }

    public static Specification<Order> inTimeSpan(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (startDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            return predicate;
        };
    }

    public static Specification<Order> build(OrderFilter filter) {
        Specification<Order> spec = Specification.unrestricted();

        if (filter.getProductId() != null)
            spec = spec.and(containsProduct(filter.getProductId()));

        if (filter.getSellerId() != null)
            spec = spec.and(hasSeller(filter.getSellerId()));

        if (filter.getProfileId() != null)
            spec = spec.and(hasBayer(filter.getProfileId()));

        if (filter.getStartDate() != null || filter.getEndDate() != null)
            spec = spec.and(inTimeSpan(filter.getStartDate(), filter.getEndDate()));

        return spec;
    }
}
