package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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
    
    @PostMapping("/send-otp-register")
    public ResponseEntity<?> sendOTPRegister(@RequestParam String email, @RequestParam String password) {
        try {
            // Check if the email already exists in Firebase or MongoDB
            if (studentService.findByEmail(email).isPresent() || firebaseUserService.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
            }

            String otp = emailService.generateAndSendOTP(email);
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to send OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp-register")
    public ResponseEntity<?> verifyOTPRegister(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailService.verifyOTP(email, otp);
        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid OTP"));
        }
    }

    @PostMapping("/register-new-user")
    public ResponseEntity<?> registerNewUser(@RequestParam String email, @RequestParam String password) {
        try {
            // Register the user in Firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            FirebaseUser firebaseUser = new FirebaseUser();
            firebaseUser.setEmail(email);
            firebaseUser.setFirebaseUid(userRecord.getUid());
            firebaseUser.setEmailVerified(false); // Email verification process not done yet
            firebaseUser.setDisplayName(userRecord.getDisplayName());
            firebaseUser.setPhotoUrl(userRecord.getPhotoUrl());
            firebaseUser.setCreatedAt(System.currentTimeMillis());
            firebaseUser.setLastLoginAt(System.currentTimeMillis());

            firebaseUserService.saveFirebaseUser(firebaseUser);

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Firebase error");
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
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
        	 UserRecord firebaseUserRecord = FirebaseAuth.getInstance().getUserByEmail(loginRequest.getEmail());

             // Step 2: Check MongoDB for user details
             Optional<Student> student = studentService.findByEmail(loginRequest.getEmail());
             if (student.isPresent()) {
                 return ResponseEntity.ok(student.get());
             } else {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in database");
             }
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Firebase error");
        }
    }

 
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleSignInRequest googleSignInRequest) throws IOException {
        try {
            String token = googleSignInRequest.getIdToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();
            
            // Check if user exists in MongoDB
            Optional<Student> student = studentService.findByFirebaseUid(firebaseUid);
            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            }

            // If user does not exist, create a new student record
            Student newStudent = new Student();
            newStudent.setFirebaseUid(firebaseUid);
            newStudent.setGoogleProfilePicture(googleSignInRequest.getProfilePicture());
            newStudent.setGoogleDisplayName(googleSignInRequest.getDisplayName());
            // You may also want to set other details like email, age, etc.
            studentService.saveStudent(newStudent, null); // No image required at this moment

            return ResponseEntity.ok(newStudent);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Firebase token");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            // Retrieve user record by email
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            
            // Create an update request to change the user's password
            UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(userRecord.getUid())
                    .setPassword(newPassword);

            // Update the user's password
            FirebaseAuth.getInstance().updateUser(updateRequest);

            // Return success message
            return ResponseEntity.ok().body("Password updated successfully");
        } catch (FirebaseAuthException e) {
            // Handle exception and return an error message
            return ResponseEntity.badRequest().body("Error updating password: " + e.getMessage());
        }
        
    }

}
