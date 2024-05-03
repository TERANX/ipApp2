package com.example.springsecurity.repository;


import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository //тот же компонент, но для понимания что класс относится к репозиторию
//тип id данной модели в дженерике
public interface UserRepository extends JpaRepository <User,Long> {
    Optional<User> findByName (String userName);

}
