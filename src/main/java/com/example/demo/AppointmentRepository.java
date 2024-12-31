package com.example.demo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
	 List<Appointment> findAllByOrderByCreatedAtDesc();
	    
	    @Query("{'doctorId': ?0, 'appointmentDate': ?1}")
	    List<Appointment> findByDoctorAndDate(String doctorId, String appointmentDate);
	    
	    // Optional: Add method to check for existing appointments
	    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
	        String doctorId, String appointmentDate, String appointmentTime);
}
