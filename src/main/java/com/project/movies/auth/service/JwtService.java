package com.project.movies.auth.service;

import com.project.movies.user.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    @Value("${application.security.jwt.expiration-remember}")
    private long jwtExpirationRememberMe;

    @Value("${application.security.jwt.refresh-token.expiration-remember}")
    private long jwtRefreshExpirationRememberMe;

    public String generateToken(UserDetails user, boolean rememberMe) {
        return buildToken(user, rememberMe ? jwtExpirationRememberMe : jwtExpiration);
    }

    public String generateRefreshToken(UserDetails user, boolean rememberMe) {
        return buildToken(user, rememberMe ? jwtRefreshExpirationRememberMe : jwtRefreshExpiration);
    }

    private String buildToken(UserDetails userDetails, final long expiration) {

        if (userDetails instanceof UserModel user) {
            return Jwts.builder()
                    .id(user.getId())
                    .claims(Map.of("role", user.getRole()))
                    .subject(user.getEmail())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey())
                    .compact();
        } else {
            throw new IllegalArgumentException("UserDetails must be an instance of UserModel");
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return email.equals(((UserModel) userDetails).getEmail()) && !isTokenExpired(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
