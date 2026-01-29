package com.bac.chatApp.dto.request.friend;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @NotNull
    private Long targetUserId;
    private String message;
}
