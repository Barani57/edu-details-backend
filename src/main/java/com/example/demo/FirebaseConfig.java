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
	        	 FileInputStream serviceAccount = new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
	            
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
