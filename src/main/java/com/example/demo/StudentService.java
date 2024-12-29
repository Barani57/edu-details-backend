package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentService {
	 @Autowired
	    private FirebaseUserRepository firebaseUserRepository;
	    
	    @Autowired
	    private StudentRepository studentRepository; 
	 
	 public Optional<Student> findByFirebaseUid(String firebaseUid) {
	        return studentRepository.findByFirebaseUid(firebaseUid);
	    }
	 
	 public Optional<Student> findByEmail(String email) {
	        return studentRepository.findByEmail(email);
	    }
	    
	 
	  public Student updateAuthenticatedStudent(Student student, MultipartFile image) throws IOException {
	        Optional<Student> existingStudent = studentRepository.findByFirebaseUid(student.getFirebaseUid());
	        
	        if (existingStudent.isPresent()) {
	            Student updatedStudent = existingStudent.get();
	            // Update fields while preserving Google auth data
	            updatedStudent.setName(student.getName());
	            updatedStudent.setAge(student.getAge());
	            updatedStudent.setAreaOfInterest(student.getAreaOfInterest());
	            updatedStudent.setDegree(student.getDegree());
	            updatedStudent.setSkills(student.getSkills());
	            updatedStudent.setCity(student.getCity());
	            updatedStudent.setMobileNumber(student.getMobileNumber());
	            
	            if (image != null && !image.isEmpty()) {
	                if (image.getSize() > 5 * 1024 * 1024) {
	                    throw new IllegalArgumentException("File size must be less than 5MB");
	                }
	                updatedStudent.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
	                updatedStudent.setImageContentType(image.getContentType());
	            }
	            
	            return studentRepository.save(updatedStudent);
	        }
	        
	        return null;
	    }
	 
	 public Student saveStudent(Student student, MultipartFile imageFile) throws IOException {
	        if (imageFile != null && !imageFile.isEmpty()) {
	            if (imageFile.getSize() > 5 * 1024 * 1024) { // 5MB check
	                throw new IllegalArgumentException("File size must be less than 5MB");
	            }
	            student.setImage(new Binary(BsonBinarySubType.BINARY, imageFile.getBytes()));
	            student.setImageContentType(imageFile.getContentType());
	        }
	        return studentRepository.save(student);
	    }
	 
	 public boolean deleteStudentIfOwner(String id, String firebaseUid) {
	        Optional<Student> student = studentRepository.findById(id);
	        if (student.isPresent() && student.get().getFirebaseUid().equals(firebaseUid)) {
	            studentRepository.deleteById(id);
	            return true;
	        }
	        return false;
	    }

	 
	    public List<Student> getAllStudents() {
	        return studentRepository.findAll();
	    }
	    
	    public Optional<Student> getStudent(String id) {
	        return studentRepository.findById(id);
	    }
	    
	    public void deleteStudent(String id) {
	        studentRepository.deleteById(id);
	    }
	    
	    public Student updateStudent(String id, Student updatedStudent, MultipartFile imageFile) throws IOException {
	        Optional<Student> existingStudent = studentRepository.findById(id);
	        
	        if (existingStudent.isPresent()) {
	            Student student = existingStudent.get();
	            student.setName(updatedStudent.getName());
	            student.setAge(updatedStudent.getAge());
	            student.setAreaOfInterest(updatedStudent.getAreaOfInterest());
	            student.setDegree(updatedStudent.getDegree());
	            student.setSkills(updatedStudent.getSkills());
	            student.setCity(updatedStudent.getCity());
	            student.setMobileNumber(updatedStudent.getMobileNumber());
	            student.setEmail(updatedStudent.getEmail());
	            
	            if (imageFile != null && !imageFile.isEmpty()) {
	                student.setImage(new Binary(BsonBinarySubType.BINARY, imageFile.getBytes()));
	                student.setImageContentType(imageFile.getContentType());
	            }
	            
	            return studentRepository.save(student);
	        }
	        return null;
	    }
}
