package com.bac.chatApp.dto.response.auth;

import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
}
