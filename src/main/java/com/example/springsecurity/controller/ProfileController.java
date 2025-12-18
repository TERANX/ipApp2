package com.example.springsecurity.controller;

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

            model.addAttribute("user", user);
            model.addAttribute("username", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userId", user.getId());

            if (user.getRoles() != null) {
                model.addAttribute("roles", user.getRoles());
            }

            // Для статистики
            model.addAttribute("registrationDate", "Сегодня");
        } else {
            System.out.println("ERROR: User not found for username: " + username);
            model.addAttribute("error", "User not found");
            return "redirect:/custom-login";
        }

        return "profile";
    }

    /**
     * Редактирование профиля (форма)
     */
    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByName(username);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("currentName", user.getName());
            model.addAttribute("currentEmail", user.getEmail());
        } else {
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
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/custom-login";
        }

        System.out.println("=== UPDATING PROFILE ===");
        System.out.println("Current user: " + currentUsername);
        System.out.println("New name: " + name);
        System.out.println("New email: " + email);

        try {
            // Проверка уникальности имени (если изменилось)
            if (!currentUsername.equals(name) && userService.existsByName(name)) {
                redirectAttributes.addFlashAttribute("error", "Username '" + name + "' already exists");
                return "redirect:/profile/edit";
            }

            // Проверка уникальности email (если изменился)
            if (!user.getEmail().equals(email) && userService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Email '" + email + "' already exists");
                return "redirect:/profile/edit";
            }

            // Обновление данных
            User updatedUser = new User();
            updatedUser.setId(user.getId());
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setPassword(user.getPassword()); // Сохраняем старый пароль
            updatedUser.setRoles(user.getRoles()); // Сохраняем роли

            userService.update(updatedUser);

            System.out.println("Profile updated successfully");
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");

            // Если изменилось имя, нужно перелогиниться
            if (!currentUsername.equals(name)) {
                redirectAttributes.addFlashAttribute("message", "Username changed. Please login again.");
                return "redirect:/logout";
            }

            return "redirect:/profile";

        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return "redirect:/profile/edit";
        }
    }

    /**
     * Смена пароля (форма)
     */
    @GetMapping("/profile/change-password")
    public String changePasswordForm() {
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

        try {
            // Проверка совпадения новых паролей
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match");
                return "redirect:/profile/change-password";
            }

            // Проверка длины нового пароля
            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "New password must be at least 6 characters");
                return "redirect:/profile/change-password";
            }

            // Смена пароля через сервис
            boolean changed = userService.changePassword(username, currentPassword, newPassword);

            if (changed) {
                System.out.println("Password changed successfully");
                redirectAttributes.addFlashAttribute("success", "Password changed successfully");

                // После смены пароля предлагаем перелогиниться
                redirectAttributes.addFlashAttribute("message", "Password changed. Please login again.");
                return "redirect:/logout";
            } else {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
                return "redirect:/profile/change-password";
            }

        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error changing password: " + e.getMessage());
            return "redirect:/profile/change-password";
        }
    }

    /**
     * Быстрый просмотр профиля (для отладки)
     */
    @GetMapping("/profile/view")
    public String viewProfile(@RequestParam(value = "userId", required = false) Long userId,
                              Model model) {
        if (userId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByName(username);

            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId != null) {
            try {
                User user = userService.getById(userId);
                model.addAttribute("user", user);
                model.addAttribute("viewMode", true);
                return "profile-view";
            } catch (Exception e) {
                model.addAttribute("error", "User not found with ID: " + userId);
            }
        }

        return "redirect:/profile";
    }
}