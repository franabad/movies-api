package com.project.movies.auth;


public class LoginRequest {
    private final String email;
    private final String password;
    private final boolean rememberMe;

    public LoginRequest(String email, String password, boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getRememberMe() {
        return this.rememberMe;
    }
}
