package com.example.springsecurity.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserPassAuth extends UsernamePasswordAuthenticationToken {
    public UserPassAuth(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserPassAuth(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
