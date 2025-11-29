package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.inputs.UserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserInput input;
    private User user;

    @BeforeEach
    void setUp() {
        input = new UserInput("Gustavo", "gustavo@test.com", "123456");
        user = new User();
        user.setId(1L);
        user.setName("Gustavo");
        user.setEmail("gustavo@test.com");
        user.setPassword("123456");
    }

    @Test
    @DisplayName("Should return all users from repository")
    void listUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.listUsers();

        assertEquals(1, result.size());
        assertEquals("Gustavo", result.getFirst().getName());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should find a user by email when it exists")
    void findUserByEmail_success() {
        when(userRepository.findByEmail("gustavo@test.com")).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail("gustavo@test.com");

        assertNotNull(result);
        assertEquals("Gustavo", result.getName());
    }

    @Test
    @DisplayName("Should throw when user by email is not found")
    void findUserByEmail_notFound_shouldThrow() {
        when(userRepository.findByEmail("gustavo@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserByEmail("gustavo@test.com"));
    }

    @Test
    @DisplayName("Should create user when email does not exist")
    void createUser_success() {
        when(userRepository.existsByEmail("gustavo@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.createUser(input);

        assertNotNull(result);
        assertEquals("Gustavo", result.getName());
        verify(userRepository).save(any());
        verify(passwordEncoder).encode("123456");
    }

    @Test
    @DisplayName("Should throw when creating user with existing email")
    void createUser_emailAlreadyExists_shouldThrow() {
        when(userRepository.existsByEmail("gustavo@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(input));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update user when user exists")
    void updateUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userRepository.save(any())).thenReturn(user);

        UserInput updatedInput = new UserInput("New Name", "new@test.com", "newPass");

        User result = userService.updateUser(1L, updatedInput);

        assertEquals("New Name", result.getName());
        assertEquals("new@test.com", result.getEmail());
        verify(passwordEncoder).encode("newPass");
    }

    @Test
    @DisplayName("Should throw when updating non-existing user")
    void updateUser_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, input));
    }

    @Test
    @DisplayName("Should delete user when user exists")
    void deleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User deleted = userService.deleteUser(1L);

        assertNotNull(deleted);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw when deleting non-existing user")
    void deleteUser_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
    }

    @Test
    @DisplayName("Should find user by ID when it exists")
    void findById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals("Gustavo", result.getName());
    }

    @Test
    @DisplayName("Should throw when finding user by non-existing ID")
    void findById_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }
}
