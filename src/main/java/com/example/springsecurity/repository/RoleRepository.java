package com.example.springsecurity.repository;

import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
