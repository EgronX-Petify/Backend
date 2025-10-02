package com.example.petify.model.product;

import com.example.petify.utils.FileStorageUtil;
import jakarta.persistence.PreRemove;

public class ProductImageListener {

    @PreRemove
    public void preRemove(ProductImage image) {
        if (image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }
    }
}
