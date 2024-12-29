package com.example.demo;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OTPRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByEmailAndOtpAndUsedFalseAndExpiryDateGreaterThan(
        String email, 
        String otp, 
        Date currentDate
    );
}
