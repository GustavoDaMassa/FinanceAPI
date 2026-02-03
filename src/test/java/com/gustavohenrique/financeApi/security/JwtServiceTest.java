package com.gustavohenrique.financeApi.security;

import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
    }

    @Test
    @DisplayName("Should generate token and extract username")
    void generateTokenAndExtractUsername() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("test@test.com", jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("Should validate a valid token")
    void isTokenValid_validToken() {
        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    @DisplayName("Should reject token with wrong username")
    void isTokenValid_wrongUser() {
        String token = jwtService.generateToken(user);

        User other = new User();
        other.setEmail("other@test.com");
        other.setRole(Role.USER);

        assertFalse(jwtService.isTokenValid(token, other));
    }

    @Test
    @DisplayName("Should reject expired token")
    void isTokenValid_expiredToken() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String expiredToken = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 200000))
                .setExpiration(new Date(System.currentTimeMillis() - 100000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        assertThrows(Exception.class, () -> jwtService.isTokenValid(expiredToken, user));
    }
}
