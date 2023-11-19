package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Mappers.UserMapper;
import com.cbfacademy.apiassessment.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/PF/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Operation(summary = "Find user by email or username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/find/{emailOrUsername}")
    public UserDTO getUser(
            @Parameter(description = "Email or username of the user")
            @PathVariable String emailOrUsername){
        return userMapper.INSTANCE.userDTO(userService.getUserByUsernameOrEmail(emailOrUsername));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().
                stream().map(userMapper.INSTANCE::userDTO).
                collect(Collectors.toList());
    }

    @Operation(summary = "Download all users as JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAllUsers/download")
    public ResponseEntity<?> downloadAllUsers() throws IOException {
        userService.getAllUsersAsJSONFile();

        Path filePath = Paths.get("src/main/resources/AllUsers.JSON");
        Resource resource = new FileSystemResource(filePath.toFile());

        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllUsers.json");

        try {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (IOException e) {
            throw new IOException("Error generating header");
        }
    }
}
