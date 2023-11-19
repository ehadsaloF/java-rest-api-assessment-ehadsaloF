package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    User saveUser(User user) throws InsufficientResourcesException, EntityExistsException;
    User updateUser(String usernameOrEmail, String name) throws EntityNotFoundException;
    User getUserByUsernameOrEmail(String usernameOrEmail) throws EntityNotFoundException;
    List<User> getAllUsers() throws EntityNotFoundException;
    void getAllUsersAsJSONFile() throws IOException;
    void deleteUser(String usernameOrEmail)  throws EntityNotFoundException;

}
