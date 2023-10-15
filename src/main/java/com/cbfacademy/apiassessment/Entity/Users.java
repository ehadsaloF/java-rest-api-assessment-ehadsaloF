package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
// To ensure all emails in the users table are unique
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class Users {
    // user_id is the primary key for the users table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    private String firstName;
    private String lastName;

    private String username;
    private String email;
    private String password;

    // one-to-many relationship between users and watchlist
    // cascade - auto update the watchlist table whenever the user table is updated
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Watchlist> watchlist;

}
