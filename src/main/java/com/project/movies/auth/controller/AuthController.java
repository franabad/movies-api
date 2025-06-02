package com.project.movies.auth.controller;

import com.project.movies.auth.LoginRequest;
import com.project.movies.auth.service.AuthService;
import com.project.movies.auth.service.JwtService;
import com.project.movies.user.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// TO DO: Check the logic in the endpoints.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        if (request.getEmail().isEmpty()) {
            return ResponseEntity.status(BAD_REQUEST).body(Map.of("message", "Email is required"));
        }

        return ResponseEntity.status(OK).body(service.login(request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserModel user) {
        final AuthResponse token = service.register(user);
        return ResponseEntity.status(CREATED).body(token);
    }

    @GetMapping("/check")
    public ResponseEntity<Object> check(@CookieValue(value = "sessionid", required = false) String sessionId) {

        if (sessionId == null) {
            return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", "No token provided"));
        }

        try {
            String username = jwtService.getEmailFromToken(sessionId);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(sessionId, userDetails)) {
                return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", "Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", "Token processing error"));
        }

        return ResponseEntity.status(OK).body(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@CookieValue(value = "sessionid", required = false) String sessionId, @CookieValue(value = "refreshsession", required = false) String refreshSession, HttpServletResponse response, HttpServletRequest request) {
        if (sessionId == null || refreshSession == null) {
            return ResponseEntity.status(UNAUTHORIZED).body(Map.of("message", "No tokens provided"));
        }

        service.refresh(sessionId, refreshSession, response, request);

        return ResponseEntity.status(OK).body(Map.of("message", "Tokens refreshed"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.status(NO_CONTENT).body(null);
    }
}
