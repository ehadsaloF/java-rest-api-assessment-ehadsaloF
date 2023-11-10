package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Service.UserService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/PF")
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    // Adds new user
    @PostMapping("/addUser")
    public Optional<User> saveNewUser( @RequestBody User newUser){
        Optional<User> user = Optional.ofNullable(userService.saveUser(newUser));
        return user;
    }


    // update user (only the user's name can be updated)
    @PutMapping("/user/update/{emailOrUsername}")
    public ResponseEntity<User> updateUser(@PathVariable String emailOrUsername, @RequestParam String name){
        User user = userService.updateUser(emailOrUsername, name);
        return ResponseEntity.ok(user);
    }

    //delete a user
    @DeleteMapping ("/user/delete/{emailOrUsername}")
    public void deleteUsers( @PathVariable String emailOrUsername){
        userService.deleteUser(emailOrUsername );
        System.out.println("User Deleted");
    }

}
