package com.example.springsecurity.service;

import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.RoleEnum;
import com.example.springsecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== DATA INITIALIZER ===");
        System.out.println("Checking and creating roles...");

        // Создаем все роли из Enum, если их нет
        Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
            String roleName = roleEnum.name(); // Преобразуем Enum в String
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Created role: " + roleName);
            } else {
                System.out.println("Role already exists: " + roleName);
            }
        });

        System.out.println("=== DATA INITIALIZATION COMPLETE ===");
    }
}