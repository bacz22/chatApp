package com.bac.chatApp.controller;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.response.ApiResponse;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;
import com.bac.chatApp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
