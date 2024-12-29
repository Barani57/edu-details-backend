package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    private Map<String, OTPData> otpStore = new HashMap<>();
    
    private static class OTPData {
        String otp;
        long timestamp;
        
        OTPData(String otp) {
            this.otp = otp;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    
    public String generateAndSendOTP(String email) throws Exception {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OTPData(otp));
        
        // Create email content
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(email);
        helper.setSubject("OTP Verification - Edu Details");
        
        // Simple email template
        String emailContent = 
                "<div style='font-family: Poppins, Arial, sans-serif; color: #333; max-width: 600px; margin: auto;'>" +
                "  <div style='background-color: #2196F3; padding: 20px; text-align: center; border-radius: 10px 10px 0 0;'>" +
                "    <h1 style='color: #fff; margin: 0;'>Edu Details</h1>" +
                "  </div>" +
                "  <div style='padding: 20px; background-color: #f9f9f9; border: 1px solid #ddd; border-top: none;'>" +
                "    <p style='font-size: 18px;'>Hello User,</p>" +
                "    <p style='font-size: 16px;'>Your One Time Password (OTP) for the Edu Details App is:</p>" +
                "    <p style='font-size: 32px; font-weight: bold; color: #2196F3; text-align: center;'>" + otp + "</p>" +
                "    <p style='font-size: 14px; color: #555;'>This OTP will expire in 5 minutes. Please do not share it with anyone.</p>" +
                "  </div>" +
                "  <div style='text-align: center; padding: 20px; background-color: #2196F3; color: #fff; border-radius: 0 0 10px 10px;'>" +
                "    <p style='margin: 0;'>Thank You,</p>" +
                "    <p style='margin: 0;'>Edu Details Team</p>" +
                "  </div>" +
                "</div>";
        
        helper.setText(emailContent, true);
        mailSender.send(message);
        
        return otp;
    }
    
    public boolean verifyOTP(String email, String otp) {
        OTPData storedData = otpStore.get(email);
        if (storedData == null) return false;
        
        // Check if OTP is expired (5 minutes validity)
        if (System.currentTimeMillis() - storedData.timestamp > 300000) {
            otpStore.remove(email);
            return false;
        }
        
        boolean isValid = storedData.otp.equals(otp);
        if (isValid) {
            otpStore.remove(email); // Remove OTP after successful verification
        }
        return isValid;
    }
}
