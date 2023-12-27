package com.cardealer.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private final String TYPE = "Bearer";
    private String accessToken;
    private String refreshToken;

}
