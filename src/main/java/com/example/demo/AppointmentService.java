package com.example.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class AppointmentService {
    
	  private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
	    
	    @Autowired
	    private AppointmentRepository appointmentRepository;
	    
	    @Transactional
	    public Appointment saveAppointment(Appointment appointment) {
	        try {
	            // Validate appointment data
	            validateAppointment(appointment);
	            
	            // Handle image URL if present
	            if (appointment.getImageUrl() != null && !appointment.getImageUrl().trim().isEmpty()) {
	                // Validate image URL format
	                validateImageUrl(appointment.getImageUrl());
	            }
	            
	            return appointmentRepository.save(appointment);
	        } catch (Exception e) {
	            logger.error("Error saving appointment: ", e);
	            throw new RuntimeException("Failed to save appointment: " + e.getMessage());
	        }
	    }
	    
	    public List<Appointment> getAllAppointments() {
	        try {
	            return appointmentRepository.findAll();
	        } catch (Exception e) {
	            logger.error("Error retrieving appointments: ", e);
	            throw new RuntimeException("Failed to retrieve appointments: " + e.getMessage());
	        }
	    }
	    
	    private void validateAppointment(Appointment appointment) {
	        if (appointment.getDoctorId() == null || appointment.getDoctorId().trim().isEmpty()) {
	            throw new IllegalArgumentException("Doctor ID is required");
	        }
	        if (appointment.getDoctorName() == null || appointment.getDoctorName().trim().isEmpty()) {
	            throw new IllegalArgumentException("Doctor name is required");
	        }
	        // Add more validations as needed
	    }
	    
	    private void validateImageUrl(String imageUrl) {
	        try {
	            new URL(imageUrl);
	        } catch (MalformedURLException e) {
	            throw new IllegalArgumentException("Invalid image URL format");
	        }
	    }
}
