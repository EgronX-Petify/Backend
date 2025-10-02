package com.example.petify.model.profile;

import com.example.petify.utils.FileStorageUtil;
import jakarta.persistence.PreRemove;

public class ProfileImageListener {

    @PreRemove
    public void preRemove(ProfileImage image) {
        if (image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }
    }
}
