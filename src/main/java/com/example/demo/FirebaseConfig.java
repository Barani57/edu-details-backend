package com.example.demo;

import org.springframework.context.annotation.Configuration;
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
		 try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json")) {
	            if (serviceAccount == null) {
	                throw new IllegalArgumentException("serviceAccountKey.json not found in resources");
	            }

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
