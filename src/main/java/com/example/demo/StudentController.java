package com.example.demo;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.auth.FirebaseAuth;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        try {
            FirebaseAuth.getInstance().generatePasswordResetLink(email);
            return ResponseEntity.ok().body("Password reset email sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending reset email");
        }
    }

    @GetMapping("/students/me")
    public ResponseEntity<Student> getCurrentUser(Principal principal) {
        return studentService.findByFirebaseUid(principal.getName())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(
            @RequestPart("student") Student student,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Principal principal) {
        try {
            student.setFirebaseUid(principal.getName());
            Student savedStudent = studentService.saveStudent(student, image);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id, Principal principal) {
        try {
            boolean deleted = studentService.deleteStudentIfOwner(id, principal.getName());
            if (deleted) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting student");
        }
    }

   

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable String id) {
        return studentService.getStudent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable String id,
            @RequestPart("student") Student student,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            Student updatedStudent = studentService.updateStudent(id, student, image);
            if (updatedStudent != null) {
                return ResponseEntity.ok(updatedStudent);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getStudentImage(@PathVariable String id) {
        Optional<Student> student = studentService.getStudent(id);
        if (student.isPresent() && student.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(student.get().getImageContentType()))
                    .body(student.get().getImage().getData());
        }
        return ResponseEntity.notFound().build();
    }

    
    @PutMapping("/profile")
    public ResponseEntity<Student> updateAuthenticatedStudentProfile(
            @RequestPart("student") Student student,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Principal principal) {
        try {
            String firebaseUid = principal.getName();
            student.setFirebaseUid(firebaseUid);
            Student savedStudent = studentService.updateAuthenticatedStudent(student, image);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(
            @RequestPart("student") Student student,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Validate required fields
            if (!isValidStudent(student)) {
                return ResponseEntity.badRequest().body(null);
            }
            Student savedStudent = studentService.saveStudent(student, image);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    private boolean isValidStudent(Student student) {
        return student.getName() != null && !student.getName().isEmpty() &&
               student.getEmail() != null && !student.getEmail().isEmpty() &&
               student.getAge() > 0 &&
               student.getAreaOfInterest() != null && !student.getAreaOfInterest().isEmpty() &&
               student.getDegree() != null && !student.getDegree().isEmpty() &&
               student.getSkills() != null && !student.getSkills().isEmpty() &&
               student.getCity() != null && !student.getCity().isEmpty() &&
               student.getMobileNumber() != null && !student.getMobileNumber().isEmpty();
    }
}
