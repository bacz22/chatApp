package com.bac.chatApp.controller;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.request.user.UserForgotPasswordRequest;
import com.bac.chatApp.dto.request.user.UserUpdateRequest;
import com.bac.chatApp.dto.response.ApiResponse;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;
import com.bac.chatApp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService iUserService;

    @PostMapping("/register")
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserCreationResponse>builder()
                .errorCode(0)
                .message("")
                .data(iUserService.createUser(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getInfo(){
        return ApiResponse.<UserResponse>builder()
                .errorCode(0)
                .message("")
                .data(iUserService.getInfo())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<UserResponse> search(@RequestParam("username") String username){
        return ApiResponse.<UserResponse>builder()
                .errorCode(0)
                .message("")
                .data(iUserService.search(username))
                .build();
    }

    @PostMapping("/me")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .errorCode(0)
                .message("")
                .data(iUserService.updateProfile(request))
                .build();
    }

    @PostMapping("/forgotPassword")
    public ApiResponse<Void> forgotPassword(@RequestBody UserForgotPasswordRequest request){
        iUserService.forgotPassword(request);
        return ApiResponse.<Void>builder()
                .errorCode(0)
                .message("password change successful")
                .build();
    }

    @PostMapping("/uploadAvatar")
    public ApiResponse<String> uploadAvatar(@RequestBody MultipartFile file){
        return ApiResponse.<String>builder()
                .errorCode(0)
                .message("")
                .data(iUserService.uploadAvatar(file))
                .build();
    }
}
