package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.request.user.UserForgotPasswordRequest;
import com.bac.chatApp.dto.request.user.UserUpdateRequest;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.User;
import com.bac.chatApp.repository.UserRepository;
import com.bac.chatApp.service.ICloudinaryService;
import com.bac.chatApp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ICloudinaryService iCloudinaryService;

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
        newUser.setHashedPassword(passwordEncoder.encode(request.getPassword()));
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

    @Override
    public UserResponse search(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        return UserResponse.toUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        // Lấy Authentication từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Lấy JWT từ principal
        Jwt jwt = (Jwt) auth.getPrincipal();

        // Lấy userId từ claims
        Long userId = jwt.getClaim("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        user.setEmail(request.getEmail());
        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());
        user.setPhone(request.getPhone());
        userRepository.save(user);
        return UserResponse.toUserResponse(user);
    }

    @Override
    public void forgotPassword(UserForgotPasswordRequest request) {
        // Lấy Authentication từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Lấy JWT từ principal
        Jwt jwt = (Jwt) auth.getPrincipal();

        // Lấy userId từ claims
        Long userId = jwt.getClaim("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userPassword = user.getPassword();
        if(!passwordEncoder.matches(request.getOldPassword(), userPassword)){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCHES);
        }
        user.setHashedPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        // Lấy Authentication từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Lấy JWT từ principal
        Jwt jwt = (Jwt) auth.getPrincipal();

        // Lấy userId từ claims
        Long userId = jwt.getClaim("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        try{
            // Kiểm tra nếu file null hoặc rỗng
            if(file == null || file.isEmpty()){
                throw new AppException(ErrorCode.FILE_IS_NULL);
            }
            // Kiểm tra kích thước file (tối đa 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new AppException(ErrorCode.FILE_TOO_LARGE);
            }
            // Kiểm tra định dạng file
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
            }
            String avatarUrl = iCloudinaryService.upload(file);
            user.setAvatarUrl(avatarUrl);
            return avatarUrl;
        }catch (IOException e){
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
