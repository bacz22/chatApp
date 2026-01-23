package com.bac.chatApp.dto.request.auth;

import lombok.Getter;

@Getter
public class LogoutRequest {
    private String accessToken;
    private String refreshToken;

}
