package com.example.petify.specfication;


import com.example.petify.dto.ecom.ProductFilter;
import com.example.petify.model.product.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filter(ProductFilter filter) {
        Specification<Product> spec = Specification.unrestricted();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and(nameContains(filter.getName()));
        }
        if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
            spec = spec.and(hasCategory(filter.getCategory()));
        }
        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            spec = spec.and(hasTags(filter.getTags()));
        }

        return spec;
    }



    public static Specification<Product> nameContains(String name) {
        return (root, query, builder) ->
            builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, builder) ->
            builder.equal(root.join("category").get("name"), category);
    }

    public static Specification<Product> hasTags(List<String> tags) {
        return (root, query, builder) -> {
            // Ensure we don't accidentally fetch duplicates
            query.distinct(true);

            // Join with tags
            var join = root.join("tags");

            // Restrict to tags in the given list
            var predicate = join.get("name").in(tags);

            // Group by product to count how many tags matched
            query.groupBy(root.get("id"));

            // Having clause: product must have matched all tags
            query.having(builder.equal(builder.countDistinct(join.get("name")), tags.size()));

            return predicate;
        };
    }
}
