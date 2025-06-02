package com.project.movies.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.movies.user.UserModel;

public class AuthResponse {

    @JsonProperty
    private UserData data;

    @JsonProperty
    private Tokens tokens;

    public AuthResponse(UserModel user, String accessToken, String refreshToken) {
        this.data = new UserData(user.getEmail(), user.getUsername());
        this.tokens = new Tokens(accessToken, refreshToken);
    }

    public static class UserData {
        private final String email;
        private final String username;

        public UserData(String email, String username) {
            this.email = email;
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }
    }

        public static class Tokens {
            @JsonProperty("access_token")
            private final String accessToken;

            @JsonProperty("refresh_token")
            private final String refreshToken;

            public Tokens(String accessToken, String refreshToken) {
                this.accessToken = accessToken;
                this.refreshToken = refreshToken;
            }

            public String getAccessToken() {
                return accessToken;
            }

            public String getRefreshToken() {
                return refreshToken;
            }
    }
}
