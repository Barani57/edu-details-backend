package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "firebase_users")
public class FirebaseUser {
    @Id
    private String id;
    private String firebaseUid;
    private String email;
    private String displayName;
    private String photoUrl;
    private boolean emailVerified;
    private long lastLoginAt;
    private long createdAt;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirebaseUid() {
		return firebaseUid;
	}
	public void setFirebaseUid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public long getLastLoginAt() {
		return lastLoginAt;
	}
	public void setLastLoginAt(long lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
    
   
}
