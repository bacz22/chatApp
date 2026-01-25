package com.bac.chatApp.dto.request.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForgotPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
