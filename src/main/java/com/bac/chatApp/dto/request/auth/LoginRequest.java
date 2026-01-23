package com.bac.chatApp.dto.request.auth;

import lombok.*;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}
