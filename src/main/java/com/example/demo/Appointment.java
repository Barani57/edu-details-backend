package com.example.demo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    private String doctorId;
    private String doctorName;
    private String speciality;
    private String hospitalName;
    private String hospitalAddress;
    private String appointmentDate;
    private String appointmentTime;
    private String imageData;
    private Date createdAt;

    // Default constructor required for MongoDB
    public Appointment() {
    	this.createdAt = new Date();
    }

    // Constructor with all fields
    public Appointment(String id, String doctorId, String doctorName, String speciality,
                      String hospitalName, String hospitalAddress, String appointmentDate,
                      String appointmentTime, String imageData) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.imageData = imageData;
    }

    

	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

}
