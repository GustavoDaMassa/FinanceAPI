package com.gustavohenrique.financeApi.security;

import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.security.dto.CreateAdminRequest;
import com.gustavohenrique.financeApi.security.dto.LoginRequest;
import com.gustavohenrique.financeApi.security.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "masterKey", "TEST_MASTER_KEY");

        user = new User();
        user.setId(1L);
        user.setName("Gustavo");
        user.setEmail("gustavo@test.com");
        user.setPassword("encoded");
        user.setRole(Role.USER);
    }

    @Test
    @DisplayName("Should authenticate with valid credentials")
    void authenticate_success() {
        LoginRequest request = new LoginRequest("gustavo@test.com", "password");
        when(userRepository.findByEmail("gustavo@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("gustavo@test.com", response.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should throw on invalid credentials")
    void authenticate_invalidCredentials() {
        LoginRequest request = new LoginRequest("gustavo@test.com", "wrong");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    @DisplayName("Should create admin with correct master key")
    void createAdmin_success() {
        CreateAdminRequest request = new CreateAdminRequest("Admin", "admin@test.com", "pass", "TEST_MASTER_KEY");
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded-pass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(2L);
            return u;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("admin-token");

        LoginResponse response = authenticationService.createAdmin(request);

        assertNotNull(response);
        assertEquals("admin-token", response.getToken());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    @DisplayName("Should throw on incorrect master key")
    void createAdmin_wrongMasterKey() {
        CreateAdminRequest request = new CreateAdminRequest("Admin", "admin@test.com", "pass", "WRONG_KEY");

        assertThrows(IllegalArgumentException.class, () -> authenticationService.createAdmin(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw on duplicate email")
    void createAdmin_duplicateEmail() {
        CreateAdminRequest request = new CreateAdminRequest("Admin", "admin@test.com", "pass", "TEST_MASTER_KEY");
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authenticationService.createAdmin(request));
        verify(userRepository, never()).save(any());
    }
}
