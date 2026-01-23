package com.bac.chatApp.controller;

import com.bac.chatApp.dto.request.auth.LoginRequest;
import com.bac.chatApp.dto.request.auth.LogoutRequest;
import com.bac.chatApp.dto.request.auth.RefreshTokenRequest;
import com.bac.chatApp.dto.response.ApiResponse;
import com.bac.chatApp.dto.response.auth.LoginResponse;
import com.bac.chatApp.service.IAuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final IAuthenticationService iAuthenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        return ApiResponse.<LoginResponse>builder()
                .errorCode(0)
                .message("")
                .data(iAuthenticationService.login(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException {
        iAuthenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .errorCode(0)
                .message("logout successful")
                .build();

    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<LoginResponse>builder()
                .errorCode(0)
                .message("")
                .data(iAuthenticationService.refreshToken(request))
                .build();
    }
}
