package com.bac.chatApp.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePasswordRequest {
    @NotBlank
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String newPassword;
}
