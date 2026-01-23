package com.bac.chatApp.service;

import com.bac.chatApp.dto.JwtInfo;
import com.bac.chatApp.dto.TokenPayload;
import com.bac.chatApp.model.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IJwtService {
    TokenPayload generateAccessToken(User user);

    TokenPayload generateRefreshToken(User user);

    boolean verifyToken(String token, String tokenType) throws ParseException, JOSEException;

    JwtInfo parseToken(String token) throws ParseException;

}
