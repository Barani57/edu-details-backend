package com.example.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	            // Remove any existing ID to ensure new document creation
	            appointment.setId(null);
	            
	            // Set creation timestamp
	            appointment.setCreatedAt(new Date());
	            
	            // Validate appointment data
	            validateAppointment(appointment);
	            
	            // Handle image URL
	            if (appointment.getImageUrl() != null && !appointment.getImageUrl().trim().isEmpty()) {
	                validateImageUrl(appointment.getImageUrl());
	            }
	            
	            // Save as new document
	            Appointment savedAppointment = appointmentRepository.save(appointment);
	            logger.info("Saved appointment with ID: " + savedAppointment.getId());
	            return savedAppointment;
	        } catch (Exception e) {
	            logger.error("Error saving appointment: ", e);
	            throw new RuntimeException("Failed to save appointment: " + e.getMessage());
	        }
	    }
	    
	    public List<Appointment> getAllAppointments() {
	        try {
	            // Sort by creation time, newest first
	            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
	            return appointmentRepository.findAll(sort);
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
