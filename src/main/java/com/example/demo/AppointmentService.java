package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    
	@Autowired
    private AppointmentRepository appointmentRepository;
    
    public Appointment saveAppointment(Appointment appointment) {
        // Validate appointment data
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        // Generate new ID if not present
        if (appointment.getId() == null || appointment.getId().isEmpty()) {
            appointment.setId(UUID.randomUUID().toString());
        }
        
        // Handle image URL
        if (appointment.getImageUrl() != null && !appointment.getImageUrl().startsWith("http")) {
            throw new IllegalArgumentException("Invalid image URL format");
        }
        
        return appointmentRepository.save(appointment);
    }
    
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            return new ArrayList<>();
        }
        return appointments;
    }
    
    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }
}
