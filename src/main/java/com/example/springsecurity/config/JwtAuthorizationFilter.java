package com.example.springsecurity.config;



import com.example.springsecurity.model.Jwt;
import com.example.springsecurity.model.UserPassAuth;
import com.example.springsecurity.service.ClaimField;
import com.example.springsecurity.service.HeaderValues;
import com.example.springsecurity.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorizationKey = request.getHeader(HeaderValues.AUTHORIZATION);
        if (Optional.ofNullable(authorizationKey).isPresent() && authorizationKey.startsWith(HeaderValues.BEARER)) {
            authorizationKey = authorizationKey.replace(HeaderValues.BEARER, "");
            try {
                if (jwtService.isValidJwt(new Jwt(authorizationKey))) {
                    Claims claims = jwtService.getClaims(authorizationKey);
                    String username = String.valueOf(claims.get(ClaimField.USERNAME));
                    List roles = claims.get(ClaimField.ROLE, List.class);
                    List<GrantedAuthority> authorities =
                            (List<GrantedAuthority>) roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authentication = new UserPassAuth(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                logger.error(e.getMessage());
                SecurityContextHolder.getContext().setAuthentication(null);
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login");
    }
}

