package com.example.springsecurity.web.dto;

import com.example.springsecurity.model.Role;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.name}")
    private String name;

//    @NotNull
//    @Size(min = 1)
//    private String matchingPassword;

//  @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    //    @ValidPassword
    @Transient
    private String password;

    @Enumerated(EnumType.STRING)
    private Role roles;

}
