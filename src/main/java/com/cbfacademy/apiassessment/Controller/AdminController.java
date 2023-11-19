package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/PF/admin")
public class AdminController {
    @Autowired
    UserService userService;


    @GetMapping("/find/{emailOrUsername}")
    public UserDTO getUser(@PathVariable String emailOrUsername){
        return userService.getUserByUsernameOrEmail(emailOrUsername);
    }

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

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
