package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface FirebaseUserRepository extends MongoRepository<FirebaseUser, String> {
    Optional<FirebaseUser> findByFirebaseUid(String firebaseUid);
    Optional<FirebaseUser> findByEmail(String email);
}
