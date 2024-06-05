package com.example.springsecurity.repository;

import com.example.springsecurity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsRepository extends JpaRepository<Option, Long> {
}
