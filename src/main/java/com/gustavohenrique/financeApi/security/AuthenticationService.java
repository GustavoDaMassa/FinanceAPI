package com.gustavohenrique.financeApi.security;

import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.security.dto.CreateAdminRequest;
import com.gustavohenrique.financeApi.security.dto.LoginRequest;
import com.gustavohenrique.financeApi.security.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.master-key:CHANGE_THIS_MASTER_KEY_IN_PRODUCTION}")
    private String masterKey;

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        return new LoginResponse(
                jwtToken,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    public LoginResponse createAdmin(CreateAdminRequest request) {
        if (!masterKey.equals(request.getMasterKey())) {
            throw new IllegalArgumentException("Invalid master key");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User admin = new User();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);

        User savedAdmin = userRepository.save(admin);

        String jwtToken = jwtService.generateToken(savedAdmin);

        return new LoginResponse(
                jwtToken,
                savedAdmin.getId(),
                savedAdmin.getEmail(),
                savedAdmin.getName(),
                savedAdmin.getRole()
        );
    }
}
