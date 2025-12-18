package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // Используем @Qualifier чтобы указать, какой именно бин использовать
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
                        .requestMatchers("/", "/index", "/reg", "/register",
                                "/login", "/custom-login", "/afterReg").permitAll()
                        .requestMatchers("/profile/**").authenticated() // Защищаем профиль
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/custom-login")           // Ваша страница логина
                        .loginProcessingUrl("/perform_login") // Куда отправлять форму
                        .usernameParameter("username")        // Имя поля username в форме
                        .passwordParameter("password")        // Имя поля password в форме
                        .defaultSuccessUrl("/profile", true)  // После успешного логина на профиль
                        .failureUrl("/custom-login?error")    // При ошибке
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/custom-login?logout")
                        .permitAll()
                )
                .build();
    }
}