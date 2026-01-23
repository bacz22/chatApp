package com.bac.chatApp.service;

import com.bac.chatApp.dto.request.auth.LoginRequest;
import com.bac.chatApp.dto.request.auth.LogoutRequest;
import com.bac.chatApp.dto.request.auth.RefreshTokenRequest;
import com.bac.chatApp.dto.response.auth.LoginResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    LoginResponse login(LoginRequest request);

    void logout(LogoutRequest request) throws ParseException;

    LoginResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
