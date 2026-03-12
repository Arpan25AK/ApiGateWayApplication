package com.example.ApiGateWayApplication.ApiDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String refreshToken;
    private String accessToken;
}
