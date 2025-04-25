package com.example.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class jwtUtil {

    private final Key secretKey;

    public jwtUtil(@Value("${jwt.secret.key}") String base64Secret) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes)");
            }
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decode JWT secret key", e);
        }
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try{
            Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
        }
        catch(JwtException e){
            throw new JwtException("Invalid JWT");
        }
    }
}
