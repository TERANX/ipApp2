package com.example.springsecurity.repository;

import com.example.springsecurity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Добавьте этот метод
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Role findByName(@Param("name") String name);

    // Или так:
    default Role findRoleByName(String name) {
        return findAll().stream()
                .filter(role -> name.equals(role.getName()))
                .findFirst()
                .orElse(null);
    }
}