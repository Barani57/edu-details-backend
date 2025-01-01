package com.example.demo;

import java.util.Base64;

import java.util.Base64;
import java.util.regex.Pattern;

public class ImageUtils {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/]*={0,2}$");

    public static void validateBase64Image(String base64Data) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            return; // No image is valid
        }

        try {
            // Extract base64 content from data URL if present
            String base64Content = extractBase64Content(base64Data);
            
            // Clean the base64 string
            base64Content = cleanBase64String(base64Content);
            
            // Validate base64 format
            if (!isValidBase64Format(base64Content)) {
                throw new IllegalArgumentException("Invalid Base64 format");
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

    private static String extractBase64Content(String base64Data) {
        if (base64Data.contains(",")) {
            String[] parts = base64Data.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid data URL format");
            }
            return parts[1];
        }
        return base64Data;
    }

    private static String cleanBase64String(String base64Content) {
        // Remove all whitespace, newlines, and carriage returns
        return base64Content.replaceAll("\\s+", "")
                          .replace("\n", "")
                          .replace("\r", "");
    }

    private static boolean isValidBase64Format(String base64Content) {
        // Check if the string length is a multiple of 4
        if (base64Content.length() % 4 != 0) {
            return false;
        }

        // Check if the string matches Base64 pattern
        return BASE64_PATTERN.matcher(base64Content).matches();
    }
    
    public static String sanitizeBase64Image(String base64Data) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            return null;
        }
        
        String base64Content = extractBase64Content(base64Data);
        return cleanBase64String(base64Content);
    }
}