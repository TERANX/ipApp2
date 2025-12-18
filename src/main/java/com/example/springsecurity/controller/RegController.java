package com.example.springsecurity.controller;

import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.RoleEnum;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class RegController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    /**
     * Показывает форму регистрации студента (USER)
     */
    @GetMapping("/reg")
    public String showRegistrationForm(Model model) {
        System.out.println("=== GET /reg ===");
        System.out.println("Showing student registration form (USER role)");
        return "reg";
    }

    /**
     * Показывает форму регистрации преподавателя (TEACHER)
     */
    @GetMapping("/teacher-reg")
    public String showTeacherRegistrationForm(Model model) {
        System.out.println("=== GET /teacher-reg ===");
        System.out.println("Showing teacher registration form (TEACHER role)");
        return "teacher-reg";
    }

    /**
     * Обрабатывает регистрацию студента (USER)
     */
    @PostMapping("/reg")
    public String registerUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("=== POST /reg ===");
        System.out.println("Student (USER) registration attempt");

        return registerUserWithRole(name, email, password, RoleEnum.USER, model, redirectAttributes, "reg");
    }

    /**
     * Обрабатывает регистрацию преподавателя (TEACHER)
     */
    @PostMapping("/teacher-reg")
    public String registerTeacher(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("=== POST /teacher-reg ===");
        System.out.println("Teacher (TEACHER) registration attempt");

        // Дополнительная валидация для подтверждения пароля (только для TEACHER)
        if (confirmPassword != null && !password.equals(confirmPassword)) {
            System.out.println("ERROR: Passwords do not match");
            model.addAttribute("error", "Пароли не совпадают");
            return "teacher-reg";
        }

        return registerUserWithRole(name, email, password, RoleEnum.TEACHER, model, redirectAttributes, "teacher-reg");
    }

    /**
     * Общий метод для регистрации с указанием роли
     */
    private String registerUserWithRole(String name, String email, String password,
                                        RoleEnum roleEnum, Model model,
                                        RedirectAttributes redirectAttributes,
                                        String templateName) {

        boolean isTeacher = roleEnum == RoleEnum.TEACHER;
        String registrationType = isTeacher ? "TEACHER" : "USER";
        String successMessage = isTeacher
                ? "Регистрация преподавателя успешна! Теперь вы можете войти в систему."
                : "Регистрация успешна! Теперь вы можете войти в систему.";

        System.out.println("=== " + registrationType + " REGISTRATION ===");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password length: " + (password != null ? password.length() : 0));
        System.out.println("Role: " + roleEnum);

        try {
            // 1. Базовая валидация
            if (name == null || name.trim().isEmpty()) {
                System.out.println("ERROR: Name is empty");
                model.addAttribute("error", "Имя пользователя обязательно");
                return templateName;
            }

            if (email == null || email.trim().isEmpty()) {
                System.out.println("ERROR: Email is empty");
                model.addAttribute("error", "Email адрес обязателен");
                return templateName;
            }

            if (password == null || password.trim().isEmpty()) {
                System.out.println("ERROR: Password is empty");
                model.addAttribute("error", "Пароль обязателен");
                return templateName;
            }

            if (password.length() < 6) {
                System.out.println("ERROR: Password too short (length: " + password.length() + ")");
                model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
                return templateName;
            }

            name = name.trim();
            email = email.trim();

            // 2. Проверка существования пользователя
            System.out.println("Checking if user '" + name + "' exists...");
            boolean userExists = userService.existsByName(name);

            if (userExists) {
                System.out.println("ERROR: User '" + name + "' already exists");
                model.addAttribute("error", "Пользователь с именем '" + name + "' уже существует");
                return templateName;
            }

            System.out.println("Checking if email '" + email + "' exists...");
            boolean emailExists = userService.existsByEmail(email);

            if (emailExists) {
                System.out.println("ERROR: Email '" + email + "' already exists");
                model.addAttribute("error", "Email адрес '" + email + "' уже используется");
                return templateName;
            }

            // 3. Находим роль в базе данных
            System.out.println("Finding role '" + roleEnum + "' in database...");
            Optional<Role> roleOpt = roleRepository.findByName(roleEnum.name());

            if (roleOpt.isEmpty()) {
                // Если роль не найдена, пытаемся создать её
                System.out.println("WARNING: Role '" + roleEnum + "' not found, attempting to create...");
                try {
                    Role newRole = new Role();
                    newRole.setName(roleEnum.name());
                    Role savedRole = roleRepository.save(newRole);
                    roleOpt = Optional.of(savedRole);
                    System.out.println("Created new role: " + roleEnum + " with ID: " + savedRole.getId());
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to create role: " + e.getMessage());
                    model.addAttribute("error", "Системная ошибка: не удалось создать роль пользователя");
                    return templateName;
                }
            }

            Role role = roleOpt.get();
            System.out.println("Using role: " + role.getName() + " (ID: " + role.getId() + ")");

            // 4. Создание пользователя
            System.out.println("Creating new User object...");
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            // Создаем Set ролей и добавляем основную роль
            user.setRoles(new HashSet<>());
            user.getRoles().add(role);

            // Если регистрируем TEACHER, можно также добавить роль USER (опционально)
            if (isTeacher) {
                Optional<Role> userRoleOpt = roleRepository.findByName(RoleEnum.USER.name());
                userRoleOpt.ifPresent(userRole -> {
                    user.getRoles().add(userRole);
                    System.out.println("Also added USER role for teacher");
                });
            }

            // 5. Сохранение пользователя
            System.out.println("Saving user to database...");
            User savedUser = userService.save(user);

            System.out.println("=== " + registrationType + " REGISTRATION SUCCESS ===");
            System.out.println("User saved with ID: " + savedUser.getId());
            System.out.println("User name: " + savedUser.getName());
            System.out.println("User email: " + savedUser.getEmail());

            if (savedUser.getRoles() != null && !savedUser.getRoles().isEmpty()) {
                String rolesString = savedUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", "));
                System.out.println("User roles: " + rolesString);
            } else {
                System.out.println("User has no roles assigned");
            }

            // 6. Перенаправление на страницу логина с соответствующим сообщением
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            String redirectParam = isTeacher ? "teacherRegistered=true" : "registered=true";
            return "redirect:/custom-login?" + redirectParam;

        } catch (Exception e) {
            System.err.println("=== " + registrationType + " REGISTRATION FAILED ===");
            e.printStackTrace();

            String errorMessage;
            String errorDetail = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

            if (errorDetail.contains("constraint") || errorDetail.contains("unique")) {
                errorMessage = "Пользователь с таким именем или email уже существует";
            } else if (errorDetail.contains("data integrity") || errorDetail.contains("duplicate")) {
                errorMessage = "Пользователь с таким именем или email уже существует";
            } else {
                errorMessage = "Ошибка регистрации: " + (e.getLocalizedMessage() != null ?
                        e.getLocalizedMessage() : "Неизвестная ошибка");
            }

            System.err.println("Error details: " + errorMessage);
            model.addAttribute("error", errorMessage);
            return templateName;
        }
    }

    /**
     * Находит или создает роль USER (для обратной совместимости)
     * @deprecated Используйте DataInitializer для создания ролей при запуске
     */
    private Role findOrCreateUserRole() {
        return findOrCreateRole(RoleEnum.USER);
    }

    /**
     * Находит или создает роль TEACHER (для обратной совместимости)
     * @deprecated Используйте DataInitializer для создания ролей при запуске
     */
    private Role findOrCreateTeacherRole() {
        return findOrCreateRole(RoleEnum.TEACHER);
    }

    /**
     * Общий метод для поиска или создания роли
     * @deprecated Используйте DataInitializer для создания ролей при запуске
     */
    private Role findOrCreateRole(RoleEnum roleEnum) {
        // Сначала ищем роль по имени
        Optional<Role> existingRole = roleRepository.findByName(roleEnum.name());

        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            System.out.println("Found " + roleEnum + " role with ID: " + role.getId());
            return role;
        } else {
            System.out.println(roleEnum + " role not found, creating new one...");
            Role newRole = new Role();
            newRole.setName(roleEnum.name());
            Role savedRole = roleRepository.save(newRole);
            System.out.println("Created " + roleEnum + " role with ID: " + savedRole.getId());
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
                    ", Roles: " + (user.getRoles() != null ?
                    user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.joining(", "))
                    : "No roles") + ")");
        }

        // Также покажем все роли в системе
        var roles = roleRepository.findAll();
        System.out.println("Total roles in DB: " + roles.size());
        for (Role role : roles) {
            System.out.println("Role: " + role.getName() + " (ID: " + role.getId() + ")");
        }

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "test-users";
    }

    /**
     * Быстрый тест регистрации студента
     */
    @GetMapping("/quick-test-student")
    public String quickTestStudent() {
        System.out.println("=== QUICK TEST STUDENT REGISTRATION ===");

        try {
            // Создаем тестового студента
            String timestamp = String.valueOf(System.currentTimeMillis());
            String name = "student_" + timestamp.substring(timestamp.length() - 5);
            String email = "student_" + timestamp.substring(timestamp.length() - 5) + "@test.com";

            return registerUserWithRole(name, email, "student123",
                    RoleEnum.USER, null, null, "redirect:/test-users");

        } catch (Exception e) {
            System.err.println("Quick test FAILED: " + e.getMessage());
            return "redirect:/test-users";
        }
    }

    /**
     * Быстрый тест регистрации преподавателя
     */
    @GetMapping("/quick-test-teacher")
    public String quickTestTeacher() {
        System.out.println("=== QUICK TEST TEACHER REGISTRATION ===");

        try {
            // Создаем тестового преподавателя
            String timestamp = String.valueOf(System.currentTimeMillis());
            String name = "teacher_" + timestamp.substring(timestamp.length() - 5);
            String email = "teacher_" + timestamp.substring(timestamp.length() - 5) + "@test.com";

            return registerUserWithRole(name, email, "teacher123",
                    RoleEnum.TEACHER, null, null, "redirect:/test-users");

        } catch (Exception e) {
            System.err.println("Quick test FAILED: " + e.getMessage());
            return "redirect:/test-users";
        }
    }

    /**
     * Просмотр всех ролей в системе
     */
    @GetMapping("/test-roles")
    public String testRoles(Model model) {
        System.out.println("=== TEST ROLES ===");

        var roles = roleRepository.findAll();
        System.out.println("Total roles in system: " + roles.size());

        for (Role role : roles) {
            System.out.println("Role: " + role.getName() + " (ID: " + role.getId() + ")");
        }

        model.addAttribute("roles", roles);
        return "test-roles";
    }
}