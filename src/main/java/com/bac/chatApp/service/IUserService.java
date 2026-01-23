package com.bac.chatApp.service;

import com.bac.chatApp.dto.request.user.UserCreationRequest;
import com.bac.chatApp.dto.response.user.UserCreationResponse;
import com.bac.chatApp.dto.response.user.UserResponse;

public interface IUserService {
    UserCreationResponse createUser(UserCreationRequest request);
    UserResponse getInfo();

}
