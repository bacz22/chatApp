package com.bac.chatApp.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Email(message = "EMAIL_INCORRECT")
    private String email;

    @NotBlank(message = "DISPLAY_NAME_NOT_BLANK")
    private String displayName;
    private String bio;
    private String phone;
}
