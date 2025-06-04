package com.ibroximjon.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "very-secret-key-for-training-app-which-is-long-enough";

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes()) // bu yerda secret string ishlatyapmiz
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return (!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String getUserName(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 kun
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }
}
