package com.example.demo;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import com.google.firebase.auth.FirebaseToken;
import java.util.Collections;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {
	private final FirebaseToken token;

    public FirebaseAuthenticationToken(FirebaseToken token) {
        super(Collections.emptyList());
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token.getUid();
    }
}
