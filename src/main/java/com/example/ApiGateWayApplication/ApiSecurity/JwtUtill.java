package com.example.ApiGateWayApplication.ApiSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtill {

    private static final String secretString = "MySuperSecretKeyForApiGatewayThatIsVeryLong";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes());

    private static final long expirationTime = 1000 * 60 * 60;

    public String generateToken(String username){
        return Jwts.builder().
                subject(username).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis() + expirationTime)).
                signWith(secretKey).
                compact();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Boolean tokenValidation(String token){
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // If the token is tampered with or expired, this throws an exception
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
