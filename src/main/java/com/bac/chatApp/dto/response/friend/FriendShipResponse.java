package com.bac.chatApp.dto.response.friend;

import com.bac.chatApp.model.FriendShip;
import com.bac.chatApp.model.FriendshipStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipResponse {
    private Long id;
    private Long userAId;
    private Long userBId;
    private Long requesterId;
    private FriendshipStatus status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long friendId;
    private String friendUsername;
    private String friendDisplayName;
    private String friendAvatarUrl;

    public static FriendShipResponse toFriendShipResponse(FriendShip entity, Long currentUserId){
        Long friendId = entity.getUserAId().equals(currentUserId)
                ? entity.getUserBId()
                : entity.getUserAId();
        return FriendShipResponse.builder()
                .id(entity.getId())
                .userAId(entity.getUserAId())
                .userBId(entity.getUserBId())
                .requesterId(entity.getRequesterId())
                .status(entity.getStatus())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .friendId(friendId)
                .build();
    }
}
