package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.DTO.Summary;
import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Mappers.UserMapper;
import com.cbfacademy.apiassessment.Service.SummaryService;
import com.cbfacademy.apiassessment.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/PF")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SummaryService summaryService;


    @Operation(summary = "Create a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "409", description = "User Already Exists",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid User Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PostMapping("/addUser")
    public UserDTO saveNewUser(
            @Parameter(description = "Name")
            @RequestParam String name,
            @Parameter(description = "Username")
            @RequestParam String username,
            @Parameter(description = "Email")
            @RequestParam String email,
            @Parameter(description = "Password")
            @RequestParam String password
    ){

            User newUser = new User(name, username, email);
            newUser.setRawPassword(password);
            return userMapper.INSTANCE.userDTO(userService.saveUser(newUser));

    }

    @Operation(summary = "Get a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User Does Not Exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/user/{emailOrUsername}")
    public UserDTO getUser(
            @Parameter(description = "Username or email of the user")
            @PathVariable String emailOrUsername){

        try{
            return userMapper.INSTANCE.userDTO(userService.getUserByUsernameOrEmail(emailOrUsername));
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    @Operation(summary = "Get User Expense and Budget Summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Summary Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Summary.class))}),
            @ApiResponse(responseCode = "404", description = "User Does Not Exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/user/{emailOrUsername}/summary")
    public Summary getUserSummary(
            @Parameter(description = "Username or email of the user")
            @PathVariable String emailOrUsername){
        try {
            return summaryService.getSummary(emailOrUsername);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Update a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PutMapping("/user/{emailOrUsername}/update")
    public UserDTO updateUser(
            @Parameter(description = "Username or email of the user")
            @PathVariable String emailOrUsername,
            @Parameter(description = "new name of the user")
            @RequestParam String name){
        try {
            return userMapper.INSTANCE.userDTO(userService.updateUser(emailOrUsername, name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Delete User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @DeleteMapping ("/user/{emailOrUsername}/delete")
    public void deleteUser(
            @Parameter(description = "Username or email of the user")
            @PathVariable String emailOrUsername){
        try{
            userService.deleteUser(emailOrUsername );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

}
