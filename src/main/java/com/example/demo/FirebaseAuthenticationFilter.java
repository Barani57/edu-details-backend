package com.example.demo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private FirebaseUserService firebaseUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = request.getHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                String idToken = authToken.substring(7);
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);                
                // Store or update user info in MongoDB
                firebaseUserService.saveOrUpdateUser(decodedToken);
                
                FirebaseAuthenticationToken authentication = new FirebaseAuthenticationToken(decodedToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}

