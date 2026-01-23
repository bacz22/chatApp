package com.bac.chatApp.dto.request.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String username;
    private String hashedPassword;
    private String email;
    private String displayName;
}
