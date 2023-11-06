package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public User saveUser(User user);
    public User updateUser(String usernameOrEmail, User user);
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail);
    public List<User> getAllUsers();
    public void deleteUser(String usernameOrEmail);

}
