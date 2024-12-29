package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.auth.FirebaseAuth;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private FirebaseUserService firebaseUserService;
    
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestParam String email) {
        try {
            // Check if email already exists
            if (studentService.findByEmail(email).isPresent() || 
                firebaseUserService.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already registered. Please login."));
            }
            
            String otp = emailService.generateAndSendOTP(email);
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to send OTP: " + e.getMessage()));
        }
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp) {
        boolean isValid = emailService.verifyOTP(email, otp);
        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid or expired OTP"));
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            // Verify email exists
            if (!studentService.findByEmail(email).isPresent() && 
                !firebaseUserService.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email not found"));
            }
            
            String otp = emailService.generateAndSendOTP(email);
            return ResponseEntity.ok(Map.of("message", "Password reset OTP sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to send OTP: " + e.getMessage()));
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        if (!emailService.verifyOTP(email, otp)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid or expired OTP"));
        }
        
        try {
            // Update password in Firebase
            FirebaseAuth.getInstance().generatePasswordResetLink(email);
            return ResponseEntity.ok(Map.of("message", "Password reset link sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to reset password"));
        }
    }
}
