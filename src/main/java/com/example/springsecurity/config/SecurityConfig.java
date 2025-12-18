package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        System.out.println("SecurityConfig initialized with MyUserDetailsService");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        System.out.println("AuthenticationProvider created with MyUserDetailsService");
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Публичные пути
                        .requestMatchers("/", "/index", "/reg", "/register",
                                "/teacher-reg", "/login", "/custom-login",
                                "/afterReg", "/css/**", "/js/**").permitAll()

                        // Пути для TEACHER (администратора)
                        .requestMatchers("/teacher/**", "/admin/**", "/tasks/create",
                                "/tasks/edit/**", "/tasks/delete/**").hasRole("TEACHER")

                        // Пути для всех аутентифицированных пользователей
                        .requestMatchers("/profile/**", "/tasks/**", "/courses/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/custom-login")
                        .loginProcessingUrl("/perform_login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler()) // Используем кастомный обработчик
                        .failureUrl("/custom-login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/custom-login?logout")
                        .permitAll()
                )
                .build();
    }

    /**
     * Кастомный обработчик успешной аутентификации
     * для редиректа TEACHER на teacher/dashboard
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {

                System.out.println("=== CUSTOM AUTHENTICATION SUCCESS HANDLER ===");
                System.out.println("User authenticated: " + authentication.getName());

                // Получаем роли пользователя
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                // Проверяем, есть ли у пользователя роль TEACHER
                boolean isTeacher = authorities.stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

                System.out.println("Is teacher: " + isTeacher);
                System.out.println("User authorities: " + authorities);

                // Редирект в зависимости от роли
                if (isTeacher) {
                    System.out.println("Redirecting TEACHER to /teacher/dashboard");
                    response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                } else {
                    System.out.println("Redirecting USER to /profile");
                    response.sendRedirect(request.getContextPath() + "/profile");
                }
            }
        };
    }
}