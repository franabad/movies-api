package com.project.movies.auth;

public class RegisterRequest {
    private final String email;
    private final String password;
    private final String username;
    private final String firstname;
    private final String lastname;

    public RegisterRequest(String email, String password, String username, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
