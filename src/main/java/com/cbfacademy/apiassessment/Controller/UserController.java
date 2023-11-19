package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.Summary;
import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Service.SummaryService;
import com.cbfacademy.apiassessment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/PF")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SummaryService summaryService;

    @PostMapping("/addUser")
    public UserDTO saveNewUser(@RequestBody User newUser){
        return userService.saveUser(newUser);
    }

    @GetMapping("/user/{emailOrUsername}")
    public UserDTO getUser(@PathVariable String emailOrUsername){
        return userService.getUserByUsernameOrEmail(emailOrUsername);
    }

    @GetMapping("/user/{emailOrUsername}/summary")
    public Summary getUserSummary(@PathVariable String emailOrUsername){
        return summaryService.getSummary(emailOrUsername);
    }

    @PutMapping("/user/{emailOrUsername}/update")
    public UserDTO updateUser(@PathVariable String emailOrUsername, @RequestParam String name){
        return userService.updateUser(emailOrUsername, name);
    }

    @DeleteMapping ("/user/{emailOrUsername}/delete")
    public void deleteUser( @PathVariable String emailOrUsername){
        userService.deleteUser(emailOrUsername );
        System.out.println("User Deleted");
    }

}
