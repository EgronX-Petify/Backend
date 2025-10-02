package com.example.petify.utils;

import com.example.petify.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStorageUtil {

    private static final String UPLOAD_DIR = "uploads";

    public static String saveFile(MultipartFile file, String subfolder) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR, subfolder);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            return subfolder + "/" + fileName;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + e.getMessage());
        }
    }

    public static byte[] loadFile(String filePath) {
        try {
            Path path = Paths.get(UPLOAD_DIR, filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FileStorageException("Failed to load file: " + e.getMessage());
        }
    }

    public static void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                Path path = Paths.get(UPLOAD_DIR, filePath);
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file: " + e.getMessage());
        }
    }
}
