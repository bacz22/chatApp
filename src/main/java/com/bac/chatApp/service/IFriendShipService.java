package com.bac.chatApp.service;

import com.bac.chatApp.dto.request.friend.FriendRequest;
import com.bac.chatApp.dto.request.friend.RespondFriendRequest;
import com.bac.chatApp.dto.response.friend.FriendShipResponse;

import java.util.List;

public interface IFriendShipService {
    FriendShipResponse sendRequest(FriendRequest request);
    FriendShipResponse respondToFriendRequest(RespondFriendRequest request);

    List<FriendShipResponse> getFriendLists();

    List<FriendShipResponse> getPendingRequest();

    List<FriendShipResponse> getSentRequest();

    void unFriend(Long friendshipId);

    FriendShipResponse block(Long targetUserId);
}
