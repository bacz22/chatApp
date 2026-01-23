package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.JwtInfo;
import com.bac.chatApp.dto.TokenPayload;
import com.bac.chatApp.dto.request.auth.LoginRequest;
import com.bac.chatApp.dto.request.auth.LogoutRequest;
import com.bac.chatApp.dto.request.auth.RefreshTokenRequest;
import com.bac.chatApp.dto.response.auth.LoginResponse;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.RedisToken;
import com.bac.chatApp.model.User;
import com.bac.chatApp.repository.RedisTokenRepository;
import com.bac.chatApp.repository.UserRepository;
import com.bac.chatApp.service.IAuthenticationService;
import com.bac.chatApp.service.IJwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final IJwtService iJwtService;
    private final RedisTokenRepository redisTokenRepository;

    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        //login
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User user = (User) authenticate.getPrincipal();

        //tao access token, refresh token
        TokenPayload accessPayload = iJwtService.generateAccessToken(user);
        TokenPayload refreshPayload = iJwtService.generateRefreshToken(user);

        Long ttl = (refreshPayload.getExpiredTime().getTime() - System.currentTimeMillis())/1000;

        //luu refresh token vao redis
        RedisToken refreshToken = RedisToken.builder()
                .id("refresh:" + refreshPayload.getJwtId())
                .jwtId(refreshPayload.getJwtId())
                .userId(user.getId().toString())
                .username(user.getUsername())
                .tokenType("refresh")
                .ttl(ttl)
                .build();

        redisTokenRepository.save(refreshToken);
        //tra ve token
        return LoginResponse.builder()
                .accessToken(accessPayload.getToken())
                .refreshToken(refreshPayload.getToken())
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException {
        // 1. Parse access token
        JwtInfo jwtInfoAccess = iJwtService.parseToken(request.getAccessToken());
        Date expiredTimeAccess = jwtInfoAccess.getExpiredTime();

        // 2. Nếu access token chưa hết hạn → Thêm vào blacklist
        if (expiredTimeAccess.after(new Date())) {
            long ttl = (expiredTimeAccess.getTime() - System.currentTimeMillis()) / 1000;

            RedisToken blacklistToken = RedisToken.builder()
                    .id("blacklist:" + jwtInfoAccess.getJwtId())
                    .jwtId(jwtInfoAccess.getJwtId())
                    .userId(jwtInfoAccess.getUserId().toString())
                    .username(jwtInfoAccess.getUsername())
                    .tokenType("blacklist")
                    .ttl(ttl)
                    .build();

            redisTokenRepository.save(blacklistToken);
        }

        // 3. Parse refresh token
        JwtInfo jwtInfoRefresh = iJwtService.parseToken(request.getRefreshToken());

        // 4. Xóa refresh token khỏi Redis
        String refreshTokenId = "refresh:" + jwtInfoRefresh.getJwtId();
        redisTokenRepository.deleteById(refreshTokenId);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        // 1. Parse refresh token
        JwtInfo jwtInfo = iJwtService.parseToken(request.getRefreshToken());

        // 2. Kiểm tra refresh token còn hạn không
        if (jwtInfo.getExpiredTime().before(new Date())) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // 3. Verify refresh token trong Redis
        if (!iJwtService.verifyToken(request.getRefreshToken(), "refresh")) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        // 4. Lấy user từ database
        User user = userRepository.findByUsername(jwtInfo.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        // 5. Generate access token mới
        TokenPayload newAccessPayload = iJwtService.generateAccessToken(user);

        // 6. Generate refresh token mới và rotate
        TokenPayload newRefreshPayload = iJwtService.generateRefreshToken(user);

        // Xóa refresh token cũ
        String oldRefreshId = "refresh:" + jwtInfo.getJwtId();
        redisTokenRepository.deleteById(oldRefreshId);

        // Lưu refresh token mới
        Long ttl = (newRefreshPayload.getExpiredTime().getTime() - System.currentTimeMillis()) / 1000;
        RedisToken newRefreshToken = RedisToken.builder()
                .id("refresh:" + newRefreshPayload.getJwtId())
                .jwtId(newRefreshPayload.getJwtId())
                .userId(user.getId().toString())
                .username(user.getUsername())
                .tokenType("refresh")
                .ttl(ttl)
                .build();
        redisTokenRepository.save(newRefreshToken);

        // 7. Return tokens mới
        return LoginResponse.builder()
                .accessToken(newAccessPayload.getToken())
                .refreshToken(newRefreshPayload.getToken())
                .build();

    }
}
