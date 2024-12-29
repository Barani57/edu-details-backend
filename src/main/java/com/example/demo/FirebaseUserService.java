package com.example.demo;

import com.google.firebase.auth.FirebaseToken;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {
    
	@Autowired
    private FirebaseUserRepository firebaseUserRepository;
    
    @Autowired
    private StudentRepository studentRepository; 
   
    
    public FirebaseUser saveOrUpdateUser(FirebaseToken decodedToken) {
        FirebaseUser user = firebaseUserRepository
            .findByFirebaseUid(decodedToken.getUid())
            .orElse(new FirebaseUser());
            
        // Update user details from Google
        user.setFirebaseUid(decodedToken.getUid());
        user.setEmail(decodedToken.getEmail());
        user.setDisplayName(decodedToken.getName());
        user.setPhotoUrl(decodedToken.getPicture());
        user.setEmailVerified(decodedToken.isEmailVerified());
        user.setLastLoginAt(System.currentTimeMillis());
        
        if (user.getCreatedAt() == 0) {
            user.setCreatedAt(System.currentTimeMillis());
            
            // Create initial student profile for Google users
            Student student = new Student();
            student.setFirebaseUid(decodedToken.getUid());
            student.setEmail(decodedToken.getEmail());
            student.setName(decodedToken.getName());
            student.setGoogleProfilePicture(decodedToken.getPicture());
            student.setGoogleDisplayName(decodedToken.getName());
            studentRepository.save(student);
        }
        
        return firebaseUserRepository.save(user);
    }
    
    public Optional<FirebaseUser> findByFirebaseUid(String firebaseUid) {
        return firebaseUserRepository.findByFirebaseUid(firebaseUid);
    }
    
    public Optional<FirebaseUser> findByEmail(String email) {
        return firebaseUserRepository.findByEmail(email);
    }
    
    public void linkStudentToFirebaseUser(String studentId, String firebaseUid) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student existingStudent = student.get();
            existingStudent.setFirebaseUid(firebaseUid);
            studentRepository.save(existingStudent);
        }
    }
}
