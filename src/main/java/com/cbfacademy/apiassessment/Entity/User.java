package com.cbfacademy.apiassessment.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expenses> expenses;

    public User(String name, String username, String email, UserRoles role, String rawPassword) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
        this.rawPassword = rawPassword;
    }

    public User() {
    }

    public String toString() {
        return "User(name=" + this.getName() + ", username=" + this.getUsername() + ", email=" + this.getEmail() + ", role=" + this.getRole() + ", rawPassword=" + this.getRawPassword() + ", budgets=" + this.getBudgets() + ", expenses=" + this.getExpenses() + ")";
    }

}
//    @Column(name = "password")
//    private String password;
//
//    // Method to encode and set the password
//    public String encodePassword(String rawPassword) {
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return passwordEncoder.encode(rawPassword);
//    }
//        this.password = encodePassword(rawPassword);
