package com.example.springsecurity.config;

import com.example.springsecurity.model.User;
import com.example.springsecurity.model.UserPassAuth;
import com.example.springsecurity.service.HeaderValues;
import com.example.springsecurity.service.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.hibernate.ObjectNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    private final JwtServiceImpl jwtService;
    private final UsernamePasswordAuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, java.io.IOException {
        if (request.getHeader("Authorization") == null) {
            String bodyJson = request.getReader().readLine();
            if (bodyJson != null) {
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(bodyJson, User.class);
                String username = user.getName();
                String password = user.getPassword();
                try {
                    Authentication authentication = new UserPassAuth(username, password);
                    authentication = authenticationProvider.authenticate(authentication);
                    String jwt = jwtService.generatedJwt(authentication);
                    response.setHeader("Authorization", HeaderValues.BEARER + jwt);
                } catch (BadCredentialsException | ObjectNotFoundException e) {
                    logger.error(e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }

}
