package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Entity.UserRoles;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;
    @Override
    // save User
    public User saveUser(User user) throws EntityExistsException{
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EntityExistsException("User with email: " + user.getEmail() + " already exist");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new EntityExistsException("User with username: " + user.getUsername() + " already exist");
        }
        user.setRole(UserRoles.USER);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String usernameOrEmail, String name) throws EntityNotFoundException {
        Optional<User> optionalUserEntity = getUserByUsernameOrEmail(usernameOrEmail);
        User existingUser = optionalUserEntity.orElse(null);
            if (existingUser != null) {
                // Update the existing user with the new data
                existingUser.setName(name);
                existingUser.setUpdatedAt();
                return userRepository.save(existingUser);
            }
            throw new EntityNotFoundException("User Does Not Exist");
    }


    @Override
    // find by username, if it's null, find by email
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) throws EntityNotFoundException{
        Optional<User> existingUser = userRepository.findByUsername(usernameOrEmail);

        if (existingUser.isEmpty()) {
            existingUser = userRepository.findByEmail(usernameOrEmail);
        }
        if (existingUser.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist");
        }

        return existingUser;
    }

    @Override
    public void getAllUsersAsJSONFile() throws IOException {
        List<User> userList = userRepository.findAll();
        String outputFile = "src/main/resources/AllUsers.JSON";
        Gson gson = new Gson();

        // Delete the existing file if it exists
        File file = new File(outputFile);
        if (file.exists()){
            file.delete();
        }

        try (FileWriter writer = new FileWriter(outputFile)) {
            // Create a new file
            gson.toJson(userList, writer);
        } catch (IOException e) {
            throw new IOException("Error generating File");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String usernameOrEmail) throws EntityNotFoundException{
        Optional<User> optionalUserEntity = getUserByUsernameOrEmail(usernameOrEmail);
        if(optionalUserEntity == null){
            throw new EntityNotFoundException("User Does not Exist");
        }
        optionalUserEntity.ifPresent(existingUser -> userRepository.delete(existingUser));
    }
}
