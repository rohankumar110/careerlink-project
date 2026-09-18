package com.rohankumar.careerlink.userservice.security;

import com.rohankumar.careerlink.userservice.entities.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTService {

    @Value("${app.security.jwt.secret-key}")
    private String jwtSecret;
    @Value("${app.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    public String generateAccessToken(User user) {

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Long extractUserId(String token) {
        String userId = extractClaim(token, Claims::getSubject);
        return Long.valueOf(userId);
    }

    public boolean isTokenValid(String token, User user) {
        Long userId = extractUserId(token);
        return (Objects.equals(userId, user.getId())) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}