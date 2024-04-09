package com.example.example.repositiry;

import com.example.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //тот же компонент, но для понимания что класс относится к репозиторию
//тип id данной модели в дженерике
public interface UserRepository extends JpaRepository <User,Long> {


}
