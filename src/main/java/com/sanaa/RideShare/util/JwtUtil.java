package com.sanaa.RideShare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private Key getSigningKey() {
        // keys.hmacShaKeyFor expects a byte[]; ensure we use a stable charset
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate a JWT token containing username and role as claims.
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate the token and return its claims. Throws runtime exceptions from the jjwt library
     * when the token is invalid or expired — callers should handle them (or let the global
     * exception handler wrap them).
     */
    public Claims validateTokenAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Convenience helpers (optional) to extract individual fields from claims
     */
    public String getUsernameFromToken(String token) {
        Claims claims = validateTokenAndGetClaims(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = validateTokenAndGetClaims(token);
        return claims.get("role", String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = validateTokenAndGetClaims(token);
        return claims.getExpiration();
    }
}