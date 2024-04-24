package com.example.springsecurity.repository;


import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //тот же компонент, но для понимания что класс относится к репозиторию
//тип id данной модели в дженерике
public interface UserRepository extends JpaRepository <User,Long> {
//    Optional<User> findByEmail (String email);
        User findByEmail (String email);

}
