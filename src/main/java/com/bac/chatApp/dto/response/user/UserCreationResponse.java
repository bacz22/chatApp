package com.bac.chatApp.dto.response.user;

import com.bac.chatApp.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationResponse {
    private String username;
    private String email;
    private String displayName;
    private LocalDateTime createdAt;

    public static UserCreationResponse toUserResponse(User user){
        return UserCreationResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
