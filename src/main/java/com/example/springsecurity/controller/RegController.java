package com.example.springsecurity.controller;

import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    /**
     * Показывает форму регистрации
     */
    @GetMapping("/reg")
    public String showRegistrationForm(Model model) {
        System.out.println("=== GET /reg ===");
        System.out.println("Showing registration form");

        // Просто возвращаем форму
        return "reg";
    }

    /**
     * Обрабатывает регистрацию пользователя
     */
    @PostMapping("/reg")
    public String registerUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("=== POST /reg ===");
        System.out.println("Registration attempt started");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password length: " + (password != null ? password.length() : 0));

        try {
            // 1. Базовая валидация
            if (name == null || name.trim().isEmpty()) {
                System.out.println("ERROR: Name is empty");
                model.addAttribute("error", "Name is required");
                return "reg";
            }

            if (email == null || email.trim().isEmpty()) {
                System.out.println("ERROR: Email is empty");
                model.addAttribute("error", "Email is required");
                return "reg";
            }

            if (password == null || password.trim().isEmpty()) {
                System.out.println("ERROR: Password is empty");
                model.addAttribute("error", "Password is required");
                return "reg";
            }

            if (password.length() < 6) {
                System.out.println("ERROR: Password too short");
                model.addAttribute("error", "Password must be at least 6 characters");
                return "reg";
            }

            name = name.trim();
            email = email.trim();

            // 2. Проверка существования пользователя
            System.out.println("Checking if user '" + name + "' exists...");
            boolean userExists = userService.existsByName(name);

            if (userExists) {
                System.out.println("ERROR: User '" + name + "' already exists");
                model.addAttribute("error", "Username '" + name + "' already exists");
                return "reg";
            }

            System.out.println("Checking if email '" + email + "' exists...");
            boolean emailExists = userService.existsByEmail(email);

            if (emailExists) {
                System.out.println("ERROR: Email '" + email + "' already exists");
                model.addAttribute("error", "Email '" + email + "' already exists");
                return "reg";
            }

            // 3. Находим или создаем роль USER
            System.out.println("Finding or creating USER role...");
            Role userRole = findOrCreateUserRole();

            // 4. Создание пользователя
            System.out.println("Creating new User object...");
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password); // Будет закодирован в UserService

            // Создаем Set ролей и добавляем роль USER
            user.setRoles(new HashSet<>());
            user.getRoles().add(userRole);

            // 5. Сохранение пользователя
            System.out.println("Saving user to database...");
            User savedUser = userService.save(user);

            System.out.println("=== REGISTRATION SUCCESS ===");
            System.out.println("User saved with ID: " + savedUser.getId());
            System.out.println("User name: " + savedUser.getName());
            System.out.println("User email: " + savedUser.getEmail());
            System.out.println("User roles count: " +
                    (savedUser.getRoles() != null ? savedUser.getRoles().size() : 0));

            // 6. Перенаправление на страницу логина
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Please login.");

            return "redirect:/custom-login?registered=true";

        } catch (Exception e) {
            System.err.println("=== REGISTRATION FAILED ===");
            e.printStackTrace();

            String errorMessage = "Registration failed: ";
            if (e.getMessage() != null && e.getMessage().contains("constraint")) {
                errorMessage += "User with this name or email already exists";
            } else if (e.getMessage() != null && e.getMessage().contains("unique constraint")) {
                errorMessage += "User with this name or email already exists";
            } else {
                errorMessage += e.getLocalizedMessage();
            }

            model.addAttribute("error", errorMessage);
            return "reg";
        }
    }

    /**
     * Находит или создает роль USER
     */
    private Role findOrCreateUserRole() {
        // Используем Optional для избежания проблемы с final переменной
        Optional<Role> existingRole = roleRepository.findAll().stream()
                .filter(role -> "USER".equalsIgnoreCase(role.getName()))
                .findFirst();

        if (existingRole.isPresent()) {
            Role userRole = existingRole.get();
            System.out.println("Found USER role with ID: " + userRole.getId());
            return userRole;
        } else {
            System.out.println("USER role not found, creating new one...");
            Role newRole = new Role();
            newRole.setName("USER");
            Role savedRole = roleRepository.save(newRole);
            System.out.println("Created USER role with ID: " + savedRole.getId());
            return savedRole;
        }
    }

    /**
     * Тестовый endpoint для проверки БД
     */
    @GetMapping("/test-users")
    public String testUsers(Model model) {
        System.out.println("=== TEST USERS ===");

        var users = userService.findAll();
        System.out.println("Total users in DB: " + users.size());

        for (User user : users) {
            System.out.println("User: " + user.getName() +
                    " (ID: " + user.getId() +
                    ", Email: " + user.getEmail() +
                    ", Roles: " + (user.getRoles() != null ? user.getRoles().size() : 0) + ")");
        }

        model.addAttribute("users", users);
        return "test-users";
    }

    /**
     * Быстрый тест регистрации
     */
    @GetMapping("/quick-test")
    public String quickTest() {
        System.out.println("=== QUICK TEST REGISTRATION ===");

        try {
            // Создаем тестового пользователя
            User testUser = new User();
            testUser.setName("testuser_" + System.currentTimeMillis());
            testUser.setEmail("test_" + System.currentTimeMillis() + "@test.com");
            testUser.setPassword("test123");

            // Добавляем роль
            Role userRole = findOrCreateUserRole();
            testUser.setRoles(new HashSet<>());
            testUser.getRoles().add(userRole);

            // Сохраняем
            User saved = userService.save(testUser);

            System.out.println("Quick test SUCCESS! User ID: " + saved.getId());
            return "redirect:/test-users";

        } catch (Exception e) {
            System.err.println("Quick test FAILED: " + e.getMessage());
            return "redirect:/test-users";
        }
    }
}