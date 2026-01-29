package com.bac.chatApp.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @NotBlank
    @Size(min = 5, message = "USERNAME_INVALID")
    private String username;

    @NotBlank
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;

    @Email(message = "EMAIL_INCORRECT")
    private String email;

    @NotBlank(message = "DISPLAY_NAME_NOT_BLANK")
    private String displayName;
}
