package com.example.springsecurity.controller;

import com.example.springsecurity.model.RoleEnum;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;

    /**
     * Главная панель преподавателя
     */
    @GetMapping("/dashboard")
    public String teacherDashboard(Model model, Authentication authentication) {
        System.out.println("=== TEACHER DASHBOARD ACCESSED ===");

        String username = authentication.getName();
        System.out.println("Teacher username: " + username);

        User user = userService.findByName(username);

        if (user == null) {
            System.out.println("ERROR: Teacher not found: " + username);
            return "redirect:/custom-login";
        }

        // Проверяем, действительно ли пользователь TEACHER
        boolean isTeacher = user.getRoles() != null &&
                user.getRoles().stream()
                        .anyMatch(role -> RoleEnum.TEACHER.name().equalsIgnoreCase(role.getName()));

        if (!isTeacher) {
            System.out.println("ACCESS DENIED: User is not TEACHER: " + username);
            System.out.println("User roles: " + (user.getRoles() != null ?
                    user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.joining(", "))
                    : "No roles"));
            return "redirect:/profile";
        }

        System.out.println("Teacher authenticated successfully: " + username);

        model.addAttribute("username", username);
        model.addAttribute("user", user);
        model.addAttribute("isTeacher", true);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("userId", user.getId());

        // Статистика (заглушки - можно добавить реальную логику)
        model.addAttribute("studentCount", 15);
        model.addAttribute("courseCount", 3);
        model.addAttribute("taskCount", 25);
        model.addAttribute("completedTasks", 120);

        return "teacher-dashboard";
    }

    /**
     * Управление студентами
     */
    @GetMapping("/students")
    public String manageStudents(Model model, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Teacher managing students: " + username);

        model.addAttribute("username", username);
        model.addAttribute("isTeacher", true);
        model.addAttribute("studentCount", 15); // Заглушка
        return "teacher-students";
    }

    /**
     * Управление курсами
     */
    @GetMapping("/courses")
    public String manageCourses(Model model, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Teacher managing courses: " + username);

        model.addAttribute("username", username);
        model.addAttribute("isTeacher", true);
        model.addAttribute("courseCount", 3); // Заглушка
        return "courses";
    }

    /**
     * Управление задачами
     */
    @GetMapping("/tasks")
    public String manageTasks(Model model, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Teacher managing tasks: " + username);

        model.addAttribute("username", username);
        model.addAttribute("isTeacher", true);
        model.addAttribute("taskCount", 25); // Заглушка
        return "tasksList";
    }

    /**
     * Создание задачи
     */
    @GetMapping("/create-task")
    public String createTask(Model model, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Teacher creating task: " + username);

        model.addAttribute("username", username);
        model.addAttribute("isTeacher", true);
        return "setTask";
    }

    /**
     * Статистика
     */
    @GetMapping("/statistics")
    public String viewStatistics(Model model, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Teacher viewing statistics: " + username);

        model.addAttribute("username", username);
        model.addAttribute("isTeacher", true);
        return "teacher-statistics";
    }

    /**
     * Профиль преподавателя (специальная страница)
     */
    @GetMapping("/profile")
    public String teacherProfile(Model model, Authentication authentication) {
        System.out.println("=== TEACHER PROFILE ===");

        String username = authentication.getName();
        User user = userService.findByName(username);

        if (user != null) {
            model.addAttribute("username", username);
            model.addAttribute("user", user);
            model.addAttribute("isTeacher", true);
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userId", user.getId());
            model.addAttribute("teacherSince", "Сегодня");
            model.addAttribute("totalStudents", 15);
            model.addAttribute("activeCourses", 3);

            return "teacher-profile";
        }

        return "redirect:/custom-login";
    }
}