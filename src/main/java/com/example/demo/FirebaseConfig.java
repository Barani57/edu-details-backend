package com.example.demo;

import org.springframework.context.annotation.Configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;


@Configuration
public class FirebaseConfig {
	 @PostConstruct
	    public void initialize() {
	        try {
	        	 String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

	             if (credentialsPath == null || credentialsPath.isEmpty()) {
	                 throw new RuntimeException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set.");
	             }

	             FileInputStream serviceAccount = new FileInputStream(credentialsPath);
	            
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
