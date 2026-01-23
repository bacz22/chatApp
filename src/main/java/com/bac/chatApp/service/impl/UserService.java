package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.User;
import com.bac.chatApp.repository.UserRepository;
import com.bac.chatApp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserCreationResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(request.getHashedPassword()));
        newUser.setDisplayName(request.getDisplayName());
        userRepository.save(newUser);

        return UserCreationResponse.toUserResponse(newUser);
    }

    @Override
    public UserResponse getInfo() {
        // Lấy Authentication từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Lấy JWT từ principal
        Jwt jwt = (Jwt) auth.getPrincipal();

        // Lấy userId từ claims
        Long userId = jwt.getClaim("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        return UserResponse.toUserResponse(user);
    }
}
