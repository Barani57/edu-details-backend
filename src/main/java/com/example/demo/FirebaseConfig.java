package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Configuration
public class FirebaseConfig {
	 @PostConstruct
	    public void initialize() {
		 try {
			 
			 String firebaseCredentials = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");

	            if (firebaseCredentials == null || firebaseCredentials.isEmpty()) {
	                throw new IllegalArgumentException("Firebase service account key not found in environment variables.");
	            }

	            // Decode the base64 string and create an InputStream
	            byte[] decodedBytes = java.util.Base64.getDecoder().decode(firebaseCredentials);
	            InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

			 
	            FirebaseOptions options = FirebaseOptions.builder()
	                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                    .build();
	        	
	            
	            if (FirebaseApp.getApps().isEmpty()) {
	                FirebaseApp.initializeApp(options);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
