package com.example.demo;

import java.lang.System.Logger;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private AppointmentService appointmentService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> bookAppointment(
            @RequestBody Appointment appointment) {
        try {
            // Validate request size
            if (appointment.getImageData() != null && 
                appointment.getImageData().length() > 7 * 1024 * 1024) { // 7MB limit for Base64
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "Image size too large"));
            }
            
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            
            // Remove image data from response to reduce payload size
            Appointment responseAppointment = new Appointment();
            BeanUtils.copyProperties(savedAppointment, responseAppointment);
            responseAppointment.setImageData(null); // Don't send image data back
            
            return ResponseEntity.ok(new ApiResponse<>(true, responseAppointment, 
                "Appointment booked successfully"));
        } catch (Exception e) {
            logger.error("Error booking appointment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, "Error booking appointment: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(new ApiResponse<>(true, appointments, 
                "Appointments retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error retrieving appointments: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, 
                    "Error retrieving appointments: " + e.getMessage()));
        }
    }
}