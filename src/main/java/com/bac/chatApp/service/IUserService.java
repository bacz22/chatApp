package com.bac.chatApp.service;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.request.user.UserChangePasswordRequest;
import com.bac.chatApp.dto.request.user.UserUpdateRequest;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    UserCreationResponse createUser(UserCreationRequest request);
    UserResponse getInfo();

    UserResponse search(String username);

    UserResponse updateProfile(UserUpdateRequest request);

    void changePassword(UserChangePasswordRequest request);

    String uploadAvatar(MultipartFile file);

}
