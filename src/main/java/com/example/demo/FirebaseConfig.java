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
		 
		 String serviceAccountKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");

	        if (serviceAccountKey == null || serviceAccountKey.isEmpty()) {
	            System.err.println("Error: FIREBASE_SERVICE_ACCOUNT_KEY environment variable is not set!");
	            return;
	        }
		 
		 try {
			 
			 InputStream serviceAccount = new ByteArrayInputStream(serviceAccountKey.getBytes(StandardCharsets.UTF_8));
			 
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
