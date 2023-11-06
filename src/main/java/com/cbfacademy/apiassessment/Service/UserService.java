package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;
    @Override
    // save User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    // update user, first get user by username or email, then update
    public User updateUser(String usernameOrEmail, User user) {
        Optional<User> optionalUserEntity = getUserByUsernameOrEmail(usernameOrEmail);
        User existingUser = optionalUserEntity.orElse(null);
         if (existingUser != null) {
             userRepository.save(existingUser);
             return existingUser;
         }
        return null;
    }

    @Override
    // find by username, if it's null, find by email
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<User> existingUser = userRepository.findByUsername(usernameOrEmail);

        if (!existingUser.isPresent()) {
            existingUser = userRepository.findByEmail(usernameOrEmail);
        }

        return existingUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String usernameOrEmail) {
        Optional<User> optionalUserEntity = getUserByUsernameOrEmail(usernameOrEmail);
        User existingUser = optionalUserEntity.orElse(null);
        if (existingUser != null) {
            userRepository.delete(existingUser);
        }
    }
}
