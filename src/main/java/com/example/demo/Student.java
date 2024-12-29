package com.example.demo;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String firebaseUid;
    private String name;
    private int age;
    private String areaOfInterest;
    private String degree;
    private String skills;
    private String city;
    private String mobileNumber;
    private String email;
    private Binary image;
    private String imageContentType;
    private String googleProfilePicture;
    private String googleDisplayName;
    
    

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirebaseUid() {
		return firebaseUid;
	}
	public void setFirebaseUid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}
	public String getGoogleProfilePicture() {
		return googleProfilePicture;
	}
	public void setGoogleProfilePicture(String googleProfilePicture) {
		this.googleProfilePicture = googleProfilePicture;
	}
	public String getGoogleDisplayName() {
		return googleDisplayName;
	}
	public void setGoogleDisplayName(String googleDisplayName) {
		this.googleDisplayName = googleDisplayName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAreaOfInterest() {
		return areaOfInterest;
	}
	public void setAreaOfInterest(String areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Binary getImage() {
		return image;
	}
	public void setImage(Binary image) {
		this.image = image;
	}
	public String getImageContentType() {
		return imageContentType;
	}
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}
}