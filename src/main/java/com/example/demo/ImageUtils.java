package com.example.demo;

import java.util.Base64;

public class ImageUtils {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    public static void validateBase64Image(String base64Data) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            return; // No image is valid
        }

        try {
            String base64Content = base64Data;
            if (base64Data.contains(",")) {
                base64Content = base64Data.split(",")[1];
            }

            // Check decoded size
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            if (decodedBytes.length > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException("Image size exceeds 5MB limit");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 image data: " + e.getMessage());
        }
    }
}