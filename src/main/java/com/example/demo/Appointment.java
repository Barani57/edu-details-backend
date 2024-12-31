package com.example.demo;

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
    private String imageUrl;

    // Default constructor required for MongoDB
    public Appointment() {}

    // Constructor with all fields
    public Appointment(String id, String doctorId, String doctorName, String speciality,
                      String hospitalName, String hospitalAddress, String appointmentDate,
                      String appointmentTime, String imageUrl) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
