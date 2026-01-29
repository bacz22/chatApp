package com.bac.chatApp.dto.request.friend;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespondFriendRequest {
    @NotNull
    private Long friendshipId;

    @NotNull
    private Boolean accept;
}
