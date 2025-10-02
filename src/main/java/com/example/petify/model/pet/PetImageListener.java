package com.example.petify.model.pet;

import com.example.petify.utils.FileStorageUtil;
import jakarta.persistence.PreRemove;

public class PetImageListener {

    @PreRemove
    public void preRemove(PetImage image) {
        if (image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }
    }
}
