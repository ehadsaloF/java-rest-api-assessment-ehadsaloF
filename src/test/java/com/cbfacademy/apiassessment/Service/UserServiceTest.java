package com.cbfacademy.apiassessment.Service;

import static org.junit.jupiter.api.Assertions.*;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Mappers.UserMapper;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("The User Service")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("can create new User")
    void testSaveUser() {

        User newUser = User.builder().username("newUser").email("example@email.com").build();

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        UserDTO createdUser = userService.saveUser(newUser);

        // Assert
        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUsername());
        assertEquals("example@email.com", createdUser.getEmail());

        // Verify that the save method of the repository was called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("can update User")
    void testUpdateUser() {
        String usernameOrEmail = "example@email.com";
        String newName = "New Name";

        User existingUser = User.builder().username("newUser").email("example@email.com").name("Old name").build();

        when(userRepository.findByEmail(usernameOrEmail)).thenReturn(Optional.ofNullable(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            if (existingUser != null) {
                existingUser.setName(savedUser.getName());
            }
            return existingUser;
        });

        // Act
        UserDTO updatedUser = userService.updateUser(usernameOrEmail, newName);

        // Assert
        assertEquals(newName, updatedUser.getName());
    }

    @Test
    @DisplayName("can get User by Email")
    void testGetUserByEmail() {
        String usernameOrEmail = "example@email.com";

        User existingUser = User.builder().username("newUser").email("example@email.com").name("Hame").build();
        UserDTO existingUserDTO = UserDTO.builder().username("newUser").email("example@email.com").name("Hame").build();

        when(userRepository.findByEmail(usernameOrEmail)).thenReturn(Optional.ofNullable(existingUser));


        // Act
        UserDTO userDTO = userService.getUserByUsernameOrEmail(usernameOrEmail);

        // Assert
        assertEquals(existingUserDTO, userDTO);
    }

    @Test
    @DisplayName("can get User by Username")
    void testGetUserByUsername() {
        String usernameOrEmail = "newUser";

        User existingUser = User.builder().username("newUser").email("example@email.com").name("Hame").build();
        UserDTO existingUserDTO = UserDTO.builder().username("newUser").email("example@email.com").name("Hame").build();

        when(userRepository.findByUsername(usernameOrEmail)).thenReturn(Optional.ofNullable(existingUser));


        // Act
        UserDTO userDTO = userService.getUserByUsernameOrEmail(usernameOrEmail);

        // Assert
        assertEquals(existingUserDTO, userDTO);
    }

    @Test
    @DisplayName("can get all Users")
    void testGetAllUsers(){
        // Arrange
        User user1 =  User.builder().
                name("User One").
                username("user1").
                email( "user1@example.com").
                build();
        User user2 =  User.builder().
                name("User Two").
                username("user2").
                email( "user2@example.com").
                build();

        UserDTO userDto1 = UserDTO.builder().
                name("User One").
                username("user1").
                email("user1@example.com").
                build();
        UserDTO userDto2 = UserDTO.builder().
                name("User Two").
                username("user2").
                email("user2@example.com").
                build();

        List<User> userList = Arrays.asList(user1, user2);
        List<UserDTO> userDtoList = Arrays.asList(userDto1, userDto2);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userDTO(user1)).thenReturn(userDto1);
        when(userMapper.userDTO(user2)).thenReturn(userDto2);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(userDtoList.size(), result.size());
        assertEquals(result, userDtoList);
    }

    @Test
    @DisplayName("can delete User")
    void testDeleteUser(){
        // Arrange
        User user1 =  User.builder().
                name("User One").
                username("user1").
                email( "user1@example.com").
                build();


        when(userRepository.findByUsername("user1")).thenReturn(Optional.ofNullable(user1));
        doNothing().when(userRepository).delete(user1);

        assertAll(() -> userService.deleteUser("user1"));
    }

}

