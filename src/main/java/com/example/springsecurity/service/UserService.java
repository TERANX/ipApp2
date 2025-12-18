package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // ============ UserDetailsService метод ============

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== USER SERVICE: loadUserByUsername() ===");
        System.out.println("Loading user: " + username);

        User user = findByName(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        System.out.println("User found: " + user.getName());
        System.out.println("User email: " + user.getEmail());
        System.out.println("User roles count: " + (user.getRoles() != null ? user.getRoles().size() : 0));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .toArray(String[]::new))
                .build();
    }

    // ============ Существующие методы ============

    public List<User> findAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
    }

    @Transactional
    public User save(User user) {
        System.out.println("=== USER SERVICE: save() ===");
        System.out.println("User name: " + user.getName());
        System.out.println("User email: " + user.getEmail());
        System.out.println("Password before encode: " + user.getPassword());

        // Кодируем пароль
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        System.out.println("Password after encode: " + encodedPassword.substring(0, 30) + "...");
        System.out.println("Roles count: " + (user.getRoles() != null ? user.getRoles().size() : 0));

        // Сохраняем
        User savedUser = repo.save(user);

        System.out.println("User saved with ID: " + savedUser.getId());
        System.out.println("=== USER SERVICE: save() COMPLETE ===");

        return savedUser;
    }

    public User delete(Long id) {
        User user = getById(id);
        repo.delete(user);
        return user;
    }

    /**
     * Проверяет существование пользователя по имени
     */
    public boolean existsByName(String name) {
        return repo.findAll().stream()
                .anyMatch(user -> user.getName().equalsIgnoreCase(name));
    }

    /**
     * Проверяет существование пользователя по email
     */
    public boolean existsByEmail(String email) {
        return repo.findAll().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Находит пользователя по имени
     */
    public User findByName(String name) {
        return repo.findAll().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // ============ НОВЫЕ МЕТОДЫ ДЛЯ ПРОФИЛЯ ============

    /**
     * Обновляет данные пользователя (кроме пароля)
     */
    @Transactional
    public void update(User user) {
        System.out.println("=== USER SERVICE: update() ===");
        System.out.println("Updating user: " + user.getName());
        System.out.println("User ID: " + user.getId());
        System.out.println("User email: " + user.getEmail());

        // Получаем существующего пользователя из БД
        User existingUser = getById(user.getId());

        // Обновляем основные поля
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        // Сохраняем обновленного пользователя
        repo.save(existingUser);

        System.out.println("User updated successfully: " + existingUser.getName());
        System.out.println("New email: " + existingUser.getEmail());
    }

    /**
     * Обновляет пароль пользователя
     */
    @Transactional
    public boolean updatePassword(Long userId, String newPassword) {
        System.out.println("=== USER SERVICE: updatePassword() ===");
        System.out.println("Updating password for user ID: " + userId);
        System.out.println("New password length: " + newPassword.length());

        User user = getById(userId);
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);

        System.out.println("Password updated successfully for user: " + user.getName());
        return true;
    }

    /**
     * Меняет пароль с проверкой текущего
     */
    @Transactional
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        System.out.println("=== USER SERVICE: changePassword() ===");
        System.out.println("Changing password for user: " + username);
        System.out.println("Current password length: " + currentPassword.length());
        System.out.println("New password length: " + newPassword.length());

        User user = findByName(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            throw new RuntimeException("User not found: " + username);
        }

        // Проверяем текущий пароль
        System.out.println("Checking current password...");
        if (encoder.matches(currentPassword, user.getPassword())) {
            System.out.println("Current password is correct");

            // Устанавливаем новый закодированный пароль
            user.setPassword(encoder.encode(newPassword));
            repo.save(user);

            System.out.println("Password changed successfully for user: " + username);
            return true;
        } else {
            System.out.println("Current password is INCORRECT for user: " + username);
            return false;
        }
    }

    /**
     * Находит пользователя по email
     */
    public User findByEmail(String email) {
        return repo.findAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Обновляет только email пользователя
     */
    @Transactional
    public void updateEmail(Long userId, String newEmail) {
        System.out.println("=== USER SERVICE: updateEmail() ===");
        System.out.println("Updating email for user ID: " + userId);
        System.out.println("New email: " + newEmail);

        User user = getById(userId);
        user.setEmail(newEmail);
        repo.save(user);

        System.out.println("Email updated successfully for user: " + user.getName());
    }

    /**
     * Обновляет только имя пользователя
     */
    @Transactional
    public void updateName(Long userId, String newName) {
        System.out.println("=== USER SERVICE: updateName() ===");
        System.out.println("Updating name for user ID: " + userId);
        System.out.println("New name: " + newName);

        User user = getById(userId);
        user.setName(newName);
        repo.save(user);

        System.out.println("Name updated successfully for user: " + newName);
    }
}