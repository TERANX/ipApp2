package com.example.springsecurity.config;

import com.example.springsecurity.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    //Пасворд энкодер используется ждя одностороннего преобразования пароля
    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//        return  new MyUserDetailsService();
//    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
//        UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").build();
//        UserDetails teacher = User.builder().username("teacher").password(encoder.encode("teacher")).roles("TEACHER").build();
//        return  new InMemoryUserDetailsManager(admin, user);
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("api/v1/apps/welcome", "api/v1/apps/new-user", "/**", "/reg").permitAll() //чтоб контрольная точка была доступна всем
                        .requestMatchers("/api/v1/apps/**").authenticated())         //для авторизованных
                .formLogin(fl -> fl.loginPage("/login").permitAll())         //разрешение доступа к форме авторизации
//                .formLogin().loginPage("/login").permitAll()
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth-> auth.requestMatchers("api/v1/apps/welcome", "api/v1/apps/new-user").permitAll() //чтоб контрольная точка была доступна всем
//                        .requestMatchers("api/v1/apps/**").authenticated())         //для авторизованных
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)          //разрешение доступа к форме авторизации
//                .build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

}
