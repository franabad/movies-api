package com.project.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.movies.jwt.jwtAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authProvider;
    private final jwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authProvider, jwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authProvider = authProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4000"); // Allow only this origin
        config.addAllowedMethod("*"); // Allow all methods
        config.addAllowedHeader("*"); // Allow all headers
        config.setAllowCredentials(true);  // So the frontend can send credentials
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Allow all GET requests
                                .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/sessions/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/cinemas/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/prices/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/reserved-seats/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/auth/logout").permitAll()

                                // Register and login
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                                // Get user's reservations and user's data
                                .requestMatchers(HttpMethod.GET, "/api/reservations/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()

                                // Create a reservation and reserve seats (buy tickets)
                                .requestMatchers(HttpMethod.POST, "/api/reservations/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/reserved-seats/**").authenticated()

                                // Check if the user is logged in
                                .requestMatchers(HttpMethod.GET, "/api/auth/check").permitAll()

                                // All PUT, PATCH, DELETE requests are allowed only for admin
                                .anyRequest().hasRole("admin")

                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
