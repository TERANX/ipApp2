package com.example.springsecurity.config;

import com.example.springsecurity.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService());
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

//"api/v1/apps/welcome", "api/v1/apps/new-user", "/**",
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InitialAuthenticationFilter initialAuthenticationFilter) throws Exception {

        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth.requestMatchers("/reg", "/index", "/login")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated());

        http.addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/reg", "/index", "/login","/").permitAll() //чтоб контрольная точка была доступна всем
//                .requestMatchers("/**").authenticated())         //для авторизованных
//                .formLogin(fl -> fl.loginPage("/login")
//                        .passwordParameter("password")
//                        .usernameParameter("name")
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/afterLoginAdmin", true)
//                        .failureUrl("/login?error=true")
//                        .permitAll())         //разрешение доступа к форме авторизации
////                .formLogin().loginPage("/login").permitAll()
//                .build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth-> auth.requestMatchers("api/v1/apps/welcome", "api/v1/apps/new-user").permitAll() //чтоб контрольная точка была доступна всем
//                        .requestMatchers("api/v1/apps/**").authenticated())         //для авторизованных
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)          //разрешение доступа к форме авторизации
//                .build();
//    }


}
