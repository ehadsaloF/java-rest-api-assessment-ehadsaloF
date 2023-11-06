package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {
    // user_id is the primary key for the users table

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoles role;

//    @Transient
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String rawPassword;

//    @Column(name = "password")
//    private String password;
//
//    // Method to encode and set the password
//    public String encodePassword(String rawPassword) {
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return passwordEncoder.encode(rawPassword);
//    }

    public User(String name, String username, String email, UserRoles role, String rawPassword) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
        this.rawPassword = rawPassword;
//        this.password = encodePassword(rawPassword);
    }
    public User(){}
}
