package com.example.springsecurity.service;

import com.example.springsecurity.model.Jwt;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String generatedJwt(Authentication authentication);
    Claims getClaims(String jwt);
    boolean isValidJwt(Jwt jwt);

}
