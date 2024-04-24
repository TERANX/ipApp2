package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service// тот же компонент
@AllArgsConstructor
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    PasswordEncoder encoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return (UserDetails) user;
    }



    public List<User> getAll() {
        return repo.findAll();
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = repo.findById(userId);
        return userFromDb.orElse(new User());
    }

//    public User getById(Long id) {
//        return repo.findById(id).orElseThrow(
//                () -> new RuntimeException(String.format("no user with id=%d founded ", id)));
//    }

    public boolean addUser(User user) {
        User userFromDB = repo.findByEmail(user.getName());

        if (userFromDB != null) {
            return false;
        }

//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(encoder().encode(user.getPassword()));
        repo.save(user);
        return true;
    }


//    public void addUser(User user){
//        user.setPassword(encoder().encode(user.getPassword()));
//        repo.save(user);
//    }

//    public User save (User user) {
//        return repo.save(user);
//    }


    public User delete (Long id) {
        User user = findUserById(id);
        repo.delete(user);
        return user;
    }

}
