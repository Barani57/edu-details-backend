package com.example.demo;

import java.lang.System.Logger;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
            @RequestBody Appointment appointment,
            @RequestHeader(value = "Content-Type", required = false) String contentType) {
        try {
            // Validate input
            if (appointment.getDoctorId() == null || appointment.getDoctorName() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "Required fields are missing"));
            }
            
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(new ApiResponse<>(true, savedAppointment, "Appointment booked successfully"));
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
            return ResponseEntity.ok(new ApiResponse<>(true, appointments, "Appointments retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error retrieving appointments: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, "Error retrieving appointments: " + e.getMessage()));
        }
    }
}
