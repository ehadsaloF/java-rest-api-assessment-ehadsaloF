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
    UserDTO saveUser(User user) throws InsufficientResourcesException, EntityExistsException;
    UserDTO updateUser(String usernameOrEmail, String name) throws EntityNotFoundException;
    UserDTO getUserByUsernameOrEmail(String usernameOrEmail) throws EntityNotFoundException;
    List<UserDTO> getAllUsers() throws EntityNotFoundException;
    void getAllUsersAsJSONFile() throws IOException;
    void deleteUser(String usernameOrEmail)  throws EntityNotFoundException;

}
