package com.project.movies.auth.service;

import com.project.movies.auth.LoginRequest;
import com.project.movies.auth.controller.AuthResponse;
import com.project.movies.config.InvalidCredentialsException;
import com.project.movies.user.UserModel;
import com.project.movies.user.IUserRepository;
import com.project.movies.utils.Logger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    // TO DO: Implement user registration logic
    public AuthResponse register(UserModel user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        UserModel savedUser = userRepository.save(user);
//
//        var accessToken = jwtService.generateToken(savedUser);
//        var refreshToken = jwtService.generateRefreshToken(savedUser);
//        return new AuthResponse(user, accessToken, refreshToken);
        return null;
    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        UserModel user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        boolean rememberMe = request.getRememberMe();

        String accessToken = jwtService.generateToken(user, rememberMe);
        String refreshToken = jwtService.generateRefreshToken(user, rememberMe);

        setTokenCookies(response, accessToken, refreshToken, rememberMe);

        return new AuthResponse(user, accessToken, refreshToken);
    }

    public void refresh(String sessionId, String refreshSession, HttpServletResponse response, HttpServletRequest request) {

        try {
            String email = jwtService.getEmailFromToken(refreshSession);
            UserModel user = userRepository.findByEmail(email).orElseThrow();

            if (!jwtService.isTokenValid(refreshSession, user)) {
                response.setStatus(UNAUTHORIZED.value());
                return;
            }

            boolean rememberMe = false;
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("sessionid") && cookie.getMaxAge() != -1) {
                    rememberMe = true;
                    break;
                }
            }

            String newAccessToken = jwtService.generateToken(user, rememberMe);
            String newRefreshToken = jwtService.generateRefreshToken(user, rememberMe);

            setTokenCookies(response, newAccessToken, newRefreshToken, rememberMe);
        } catch (Exception e) {
            response.setStatus(UNAUTHORIZED.value());
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        request.getSession().invalidate();

        Cookie accessTokenCookie = new Cookie("sessionid", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // WARNING - Change this to true in production
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshsession", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // WARNING - Change this to true in production
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }

    private void setTokenCookies(HttpServletResponse response, String sessionId, String refreshSession, boolean rememberMe) {

        int sessionIdMaxAge = rememberMe ? 60 * 15 : -1; // 15 minutos
        int refreshSessionMaxAge = rememberMe ? 60 * 60 * 24 * 30 : -1; // 30 dias

        Cookie sessionIdCookie = new Cookie("sessionid", sessionId);
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setSecure(false); // WARNING - Change this to true in production
        sessionIdCookie.setPath("/");
        sessionIdCookie.setAttribute("sameSite", "strict");
        sessionIdCookie.setMaxAge(sessionIdMaxAge);

        Cookie refreshSessionCookie = new Cookie("refreshsession", refreshSession);
        refreshSessionCookie.setHttpOnly(true);
        refreshSessionCookie.setSecure(false); // WARNING - Change this to true in production
        refreshSessionCookie.setPath("/");
        refreshSessionCookie.setAttribute("sameSite", "strict");
        refreshSessionCookie.setMaxAge(refreshSessionMaxAge);

        response.addCookie(sessionIdCookie);
        response.addCookie(refreshSessionCookie);
    }
}
