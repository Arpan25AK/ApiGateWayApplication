package com.example.ApiGateWayApplication.ApiSecurity;

import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtill jwtutill;

    public JwtFilter(JwtUtill jwtutill){
        this.jwtutill = jwtutill;
    }
}
