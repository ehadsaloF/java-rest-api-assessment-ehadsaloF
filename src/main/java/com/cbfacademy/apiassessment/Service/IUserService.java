package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    public User saveUser(User user) throws InsufficientResourcesException, EntityExistsException;
    public User updateUser(String usernameOrEmail, String name) throws EntityNotFoundException;
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) throws EntityNotFoundException;
    public List<User> getAllUsers();
    public void getAllUsersAsJSONFile() throws IOException;
    public void deleteUser(String usernameOrEmail)  throws EntityNotFoundException;

}
