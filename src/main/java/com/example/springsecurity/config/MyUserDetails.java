package com.example.springsecurity.config;

//import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private  User user;
    public MyUserDetails (User user){
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(", ")) //сплитим строку роли на отдельные кусочки
                .map(SimpleGrantedAuthority::new) //преобразуем строковое значение в нужный класс
                .collect(Collectors.toList()); //собираем все роли в лист
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //истек ли срок действия аккаунта, true если не истек

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } //заблокирован  ли пользователь, true если не заблокирован

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } //Истек ли срок действия пароля, true если учетные данные пользователя действительны

    @Override
    public boolean isEnabled() {
        return true;
    } // включен ли пользователь, true если включен
}
