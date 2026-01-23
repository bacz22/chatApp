package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.JwtInfo;
import com.bac.chatApp.dto.TokenPayload;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.RedisToken;
import com.bac.chatApp.model.User;
import com.bac.chatApp.repository.RedisTokenRepository;
import com.bac.chatApp.service.IJwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final RedisTokenRepository redisTokenRepository;

    @Override
    public TokenPayload generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expiredTime = Date.from(issueTime.toInstant().plus(15, ChronoUnit.MINUTES));
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .claim("type", "access token")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        String token = jwsObject.serialize();
        return TokenPayload.builder()
                .token(token)
                .jwtId(jwtId)
                .expiredTime(expiredTime)
                .build();
    }

    @Override
    public TokenPayload generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expiredTime = Date.from(issueTime.toInstant().plus(30, ChronoUnit.DAYS));
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .claim("type", "refresh token")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        String token = jwsObject.serialize();
        return TokenPayload.builder()
                .token(token)
                .jwtId(jwtId)
                .expiredTime(expiredTime)
                .build();
    }

    @Override
    public boolean verifyToken(String token, String tokenType) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 1. Verify signature
        if (!signedJWT.verify(new MACVerifier(secretKey))) {
            return false;
        }

        // 2. Check expiration
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) {
            return false;
        }

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        // 3. Logic khác nhau cho access và refresh token
        if ("access".equals(tokenType)) {
            // Access token: Kiểm tra KHÔNG có trong blacklist
            Optional<RedisToken> blacklisted = redisTokenRepository.findById("blacklist:" + jwtId);
            return blacklisted.isEmpty();

        } else if ("refresh".equals(tokenType)) {
            // Refresh token: PHẢI CÓ trong Redis
            Optional<RedisToken> refreshToken = redisTokenRepository.findById("refresh:" + jwtId);
            return refreshToken.isPresent();
        }

        return false;
    }

    @Override
    public JwtInfo parseToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String username = signedJWT.getJWTClaimsSet().getSubject();
        String type = (String) signedJWT.getJWTClaimsSet().getClaim("type");
        Long userId = signedJWT.getJWTClaimsSet().getLongClaim("userId");

        return JwtInfo.builder()
                .jwtId(jwtId)
                .issueTime(issueTime)
                .expiredTime(expiredTime)
                .username(username)
                .userId(userId)
                .type(type)
                .build();
    }
}
