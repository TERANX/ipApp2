package com.example.springsecurity.controller;

import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.RoleEnum;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    /**
     * Личный кабинет пользователя
     */
    @GetMapping("/profile")
    public String userProfile(Model model) {
        System.out.println("=== PROFILE PAGE ACCESSED ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Current user: " + username);

        // Получаем информацию о пользователе
        User user = userService.findByName(username);

        if (user != null) {
            System.out.println("User found: ID=" + user.getId() + ", Email=" + user.getEmail());

            // Проверяем роли пользователя
            boolean isTeacher = false;
            boolean isAdmin = false;
            String userRoles = "No roles";

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                isTeacher = user.getRoles().stream()
                        .anyMatch(role -> RoleEnum.TEACHER.name().equalsIgnoreCase(role.getName()));
                isAdmin = user.getRoles().stream()
                        .anyMatch(role -> RoleEnum.ADMIN.name().equalsIgnoreCase(role.getName()));

                userRoles = user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", "));
            }

            System.out.println("User roles: " + userRoles);
            System.out.println("Is teacher: " + isTeacher);
            System.out.println("Is admin: " + isAdmin);

            model.addAttribute("user", user);
            model.addAttribute("username", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userId", user.getId());
            model.addAttribute("isTeacher", isTeacher);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("userRoles", userRoles);

            if (user.getRoles() != null) {
                model.addAttribute("roles", user.getRoles());
            }

            // Статистика пользователя (можно расширить)
            model.addAttribute("registrationDate", "Сегодня");
            model.addAttribute("completedTasks", 0);
            model.addAttribute("totalTasks", 0);
            model.addAttribute("progressPercentage", 0);

            // Ссылки для TEACHER
            if (isTeacher) {
                model.addAttribute("teacherDashboardUrl", "/teacher/dashboard");
                model.addAttribute("teacherStudentsUrl", "/teacher/students");
                model.addAttribute("teacherCoursesUrl", "/teacher/courses");
                model.addAttribute("teacherTasksUrl", "/teacher/tasks");
            }

            if (isTeacher) {
                model.addAttribute("teacherDashboardUrl", "/teacher/dashboard");
                model.addAttribute("teacherProfileUrl", "/teacher/profile");
            }

            // Ссылки для ADMIN
            if (isAdmin) {
                model.addAttribute("adminDashboardUrl", "/admin/dashboard");
            }

            System.out.println("Profile data loaded successfully for user: " + username);

        } else {
            System.out.println("ERROR: User not found for username: " + username);
            model.addAttribute("error", "Пользователь не найден");
            return "redirect:/custom-login";
        }

        return "profile";
    }

    /**
     * Редактирование профиля (форма)
     */
    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        System.out.println("=== PROFILE EDIT FORM ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            System.out.println("Loading edit form for user: " + username);

            model.addAttribute("user", user);
            model.addAttribute("currentName", user.getName());
            model.addAttribute("currentEmail", user.getEmail());
            model.addAttribute("userId", user.getId());

            // Проверяем роли для отображения дополнительной информации
            boolean isTeacher = user.getRoles() != null &&
                    user.getRoles().stream()
                            .anyMatch(role -> RoleEnum.TEACHER.name().equalsIgnoreCase(role.getName()));
            model.addAttribute("isTeacher", isTeacher);

        } else {
            System.out.println("ERROR: User not found for editing: " + username);
            return "redirect:/custom-login";
        }

        return "edit-profile";
    }

    /**
     * Обновление профиля
     */
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User user = userService.findByName(currentUsername);

        if (user == null) {
            System.out.println("ERROR: User not found during update: " + currentUsername);
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден");
            return "redirect:/custom-login";
        }

        System.out.println("=== UPDATING PROFILE ===");
        System.out.println("Current user: " + currentUsername);
        System.out.println("User ID: " + user.getId());
        System.out.println("New name: " + name);
        System.out.println("New email: " + email);

        try {
            // 1. Проверка входных данных
            if (name == null || name.trim().isEmpty()) {
                System.out.println("ERROR: New name is empty");
                redirectAttributes.addFlashAttribute("error", "Имя пользователя не может быть пустым");
                return "redirect:/profile/edit";
            }

            if (email == null || email.trim().isEmpty()) {
                System.out.println("ERROR: New email is empty");
                redirectAttributes.addFlashAttribute("error", "Email адрес не может быть пустым");
                return "redirect:/profile/edit";
            }

            name = name.trim();
            email = email.trim();

            // 2. Проверка уникальности имени (если изменилось)
            if (!currentUsername.equals(name)) {
                System.out.println("Username changed, checking availability...");
                boolean nameExists = userService.existsByName(name);

                if (nameExists) {
                    System.out.println("ERROR: New username already exists: " + name);
                    redirectAttributes.addFlashAttribute("error", "Имя пользователя '" + name + "' уже занято");
                    return "redirect:/profile/edit";
                }
            }

            // 3. Проверка уникальности email (если изменился)
            if (!user.getEmail().equals(email)) {
                System.out.println("Email changed, checking availability...");
                boolean emailExists = userService.existsByEmail(email);

                if (emailExists) {
                    System.out.println("ERROR: New email already exists: " + email);
                    redirectAttributes.addFlashAttribute("error", "Email адрес '" + email + "' уже используется");
                    return "redirect:/profile/edit";
                }
            }

            // 4. Создаем обновленного пользователя
            System.out.println("Creating updated user object...");
            User updatedUser = new User();
            updatedUser.setId(user.getId());
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setPassword(user.getPassword()); // Сохраняем существующий пароль
            updatedUser.setRoles(user.getRoles()); // Сохраняем роли

            // 5. Сохраняем изменения
            System.out.println("Saving updated user...");
            userService.update(updatedUser);

            System.out.println("Profile updated successfully");
            System.out.println("New username: " + name);
            System.out.println("New email: " + email);

            redirectAttributes.addFlashAttribute("success", "Профиль успешно обновлен");

            // 6. Если изменилось имя пользователя, нужно перелогиниться
            if (!currentUsername.equals(name)) {
                System.out.println("Username changed, requiring re-login");
                redirectAttributes.addFlashAttribute("message", "Имя пользователя изменено. Пожалуйста, войдите снова.");
                return "redirect:/logout";
            }

            return "redirect:/profile";

        } catch (Exception e) {
            System.err.println("ERROR updating profile: " + e.getMessage());
            e.printStackTrace();

            String errorMessage = "Ошибка при обновлении профиля: ";
            if (e.getMessage() != null && e.getMessage().contains("constraint")) {
                errorMessage += "Пользователь с таким именем или email уже существует";
            } else {
                errorMessage += e.getLocalizedMessage() != null ?
                        e.getLocalizedMessage() : "Неизвестная ошибка";
            }

            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/profile/edit";
        }
    }

    /**
     * Смена пароля (форма)
     */
    @GetMapping("/profile/change-password")
    public String changePasswordForm(Model model) {
        System.out.println("=== CHANGE PASSWORD FORM ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            model.addAttribute("username", username);
            model.addAttribute("userId", user.getId());
        }

        return "change-password";
    }

    /**
     * Обновление пароля
     */
    @PostMapping("/profile/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        System.out.println("=== CHANGING PASSWORD ===");
        System.out.println("User: " + username);
        System.out.println("Current password length: " + currentPassword.length());
        System.out.println("New password length: " + newPassword.length());

        try {
            // 1. Проверка входных данных
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                System.out.println("ERROR: Current password is empty");
                redirectAttributes.addFlashAttribute("error", "Текущий пароль не может быть пустым");
                return "redirect:/profile/change-password";
            }

            if (newPassword == null || newPassword.trim().isEmpty()) {
                System.out.println("ERROR: New password is empty");
                redirectAttributes.addFlashAttribute("error", "Новый пароль не может быть пустым");
                return "redirect:/profile/change-password";
            }

            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                System.out.println("ERROR: Confirm password is empty");
                redirectAttributes.addFlashAttribute("error", "Подтверждение пароля не может быть пустым");
                return "redirect:/profile/change-password";
            }

            currentPassword = currentPassword.trim();
            newPassword = newPassword.trim();
            confirmPassword = confirmPassword.trim();

            // 2. Проверка совпадения новых паролей
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("ERROR: New passwords do not match");
                redirectAttributes.addFlashAttribute("error", "Новый пароль и подтверждение не совпадают");
                return "redirect:/profile/change-password";
            }

            // 3. Проверка длины нового пароля
            if (newPassword.length() < 6) {
                System.out.println("ERROR: New password too short (length: " + newPassword.length() + ")");
                redirectAttributes.addFlashAttribute("error", "Новый пароль должен содержать минимум 6 символов");
                return "redirect:/profile/change-password";
            }

            // 4. Проверка, что новый пароль отличается от текущего
            if (newPassword.equals(currentPassword)) {
                System.out.println("ERROR: New password is the same as current password");
                redirectAttributes.addFlashAttribute("error", "Новый пароль должен отличаться от текущего");
                return "redirect:/profile/change-password";
            }

            // 5. Смена пароля через сервис
            System.out.println("Calling userService.changePassword()...");
            boolean changed = userService.changePassword(username, currentPassword, newPassword);

            if (changed) {
                System.out.println("Password changed successfully for user: " + username);
                redirectAttributes.addFlashAttribute("success", "Пароль успешно изменен");

                // После смены пароля предлагаем перелогиниться
                redirectAttributes.addFlashAttribute("message", "Пароль изменен. Пожалуйста, войдите снова.");
                return "redirect:/logout";
            } else {
                System.out.println("Current password is incorrect for user: " + username);
                redirectAttributes.addFlashAttribute("error", "Текущий пароль указан неверно");
                return "redirect:/profile/change-password";
            }

        } catch (Exception e) {
            System.err.println("ERROR changing password: " + e.getMessage());
            e.printStackTrace();

            String errorMessage = "Ошибка при смене пароля: ";
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                errorMessage += "Пользователь не найден";
            } else {
                errorMessage += e.getLocalizedMessage() != null ?
                        e.getLocalizedMessage() : "Неизвестная ошибка";
            }

            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/profile/change-password";
        }
    }

    /**
     * Просмотр профиля другого пользователя (для TEACHER/ADMIN)
     */
    @GetMapping("/profile/view")
    public String viewOtherProfile(
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {

        System.out.println("=== VIEW OTHER PROFILE ===");

        // Получаем текущего пользователя для проверки прав
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User currentUser = userService.findByName(currentUsername);

        if (currentUser == null) {
            System.out.println("ERROR: Current user not found: " + currentUsername);
            return "redirect:/custom-login";
        }

        // Проверяем, является ли текущий пользователь TEACHER или ADMIN
        boolean isTeacher = currentUser.getRoles() != null &&
                currentUser.getRoles().stream()
                        .anyMatch(role -> RoleEnum.TEACHER.name().equalsIgnoreCase(role.getName()));
        boolean isAdmin = currentUser.getRoles() != null &&
                currentUser.getRoles().stream()
                        .anyMatch(role -> RoleEnum.ADMIN.name().equalsIgnoreCase(role.getName()));

        if (!isTeacher && !isAdmin) {
            System.out.println("ACCESS DENIED: User is not TEACHER or ADMIN");
            return "redirect:/profile";
        }

        // Если userId не указан, показываем профиль текущего пользователя
        if (userId == null) {
            System.out.println("No userId specified, redirecting to own profile");
            return "redirect:/profile";
        }

        try {
            System.out.println("Viewing profile for user ID: " + userId);
            User targetUser = userService.getById(userId);

            if (targetUser != null) {
                System.out.println("Target user found: " + targetUser.getName());

                model.addAttribute("user", targetUser);
                model.addAttribute("username", targetUser.getName());
                model.addAttribute("email", targetUser.getEmail());
                model.addAttribute("userId", targetUser.getId());
                model.addAttribute("viewMode", true);
                model.addAttribute("isCurrentUserTeacherOrAdmin", true);

                if (targetUser.getRoles() != null) {
                    model.addAttribute("roles", targetUser.getRoles());
                    String userRoles = targetUser.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.joining(", "));
                    model.addAttribute("userRoles", userRoles);
                }

                return "profile-view";
            } else {
                System.out.println("ERROR: Target user not found with ID: " + userId);
                model.addAttribute("error", "Пользователь не найден");
                return "redirect:/profile";
            }

        } catch (Exception e) {
            System.err.println("ERROR viewing profile: " + e.getMessage());
            model.addAttribute("error", "Ошибка при просмотре профиля: " + e.getMessage());
            return "redirect:/profile";
        }
    }

    /**
     * Статистика пользователя
     */
    @GetMapping("/profile/statistics")
    public String userStatistics(Model model) {
        System.out.println("=== USER STATISTICS ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            model.addAttribute("username", username);
            model.addAttribute("userId", user.getId());

            // Здесь можно добавить реальную статистику
            model.addAttribute("completedTasks", 0);
            model.addAttribute("inProgressTasks", 0);
            model.addAttribute("totalTasks", 0);
            model.addAttribute("averageScore", 0);
            model.addAttribute("rank", "Новичок");
            model.addAttribute("progressPercentage", 0);

            return "profile-statistics";
        } else {
            return "redirect:/custom-login";
        }
    }

    /**
     * Настройки пользователя
     */
    @GetMapping("/profile/settings")
    public String userSettings(Model model) {
        System.out.println("=== USER SETTINGS ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            model.addAttribute("username", username);
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userId", user.getId());

            // Проверяем роль TEACHER для дополнительных настроек
            boolean isTeacher = user.getRoles() != null &&
                    user.getRoles().stream()
                            .anyMatch(role -> RoleEnum.TEACHER.name().equalsIgnoreCase(role.getName()));
            model.addAttribute("isTeacher", isTeacher);

            return "profile-settings";
        } else {
            return "redirect:/custom-login";
        }
    }

    /**
     * Тестовая страница для проверки данных профиля
     */
    @GetMapping("/profile/test")
    public String profileTest(Model model) {
        System.out.println("=== PROFILE TEST PAGE ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            System.out.println("Test user data:");
            System.out.println("ID: " + user.getId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Password hash: " + (user.getPassword() != null ?
                    user.getPassword().substring(0, 30) + "..." : "null"));

            if (user.getRoles() != null) {
                System.out.println("Roles (" + user.getRoles().size() + "):");
                user.getRoles().forEach(role ->
                        System.out.println("  - " + role.getName() + " (ID: " + role.getId() + ")"));
            } else {
                System.out.println("Roles: null");
            }

            model.addAttribute("user", user);
            model.addAttribute("userJson", user.toString()); // Для отладки

            return "profile-test";
        } else {
            System.out.println("ERROR: User not found for test");
            return "redirect:/custom-login";
        }
    }
}