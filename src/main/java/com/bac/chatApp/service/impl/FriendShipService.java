package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.request.friend.FriendRequest;
import com.bac.chatApp.dto.request.friend.RespondFriendRequest;
import com.bac.chatApp.dto.response.friend.FriendShipResponse;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.FriendShip;
import com.bac.chatApp.model.FriendshipStatus;
import com.bac.chatApp.repository.FriendShipRepository;
import com.bac.chatApp.repository.UserRepository;
import com.bac.chatApp.service.IFriendShipService;
import com.bac.chatApp.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendShipService implements IFriendShipService {

    private final FriendShipRepository friendShipRepository;
    private final UserRepository userRepository;

    private final INotificationService iNotificationService;

    @Override
    public FriendShipResponse sendRequest(FriendRequest request) {

        Long currentUserId = getCurrentUserId();
        Long targetUserId = request.getTargetUserId();

        //kh tự gửi kb cho chính mình
        if(currentUserId.equals(targetUserId)){
            throw new AppException(ErrorCode.CANNOT_FRIEND_YOURSELF);
        }

        //ng nhận lời có tồn tại kh
        userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        Optional<FriendShip> existingFriendShip = friendShipRepository.findByUserPair(currentUserId, targetUserId);
        if(existingFriendShip.isPresent()){
            FriendShip fs = existingFriendShip.get();
            switch (fs.getStatus()) {
                case ACCEPTED -> throw new AppException(ErrorCode.ALREADY_FRIENDS);
                case PENDING -> throw new AppException(ErrorCode.REQUEST_ALREADY_SENT);
                case BLOCKED -> throw new AppException(ErrorCode.USER_BLOCKED);
            }
        }

        FriendShip friendShip = FriendShip.builder()
                .userAId(Math.min(currentUserId, targetUserId))
                .userBId(Math.max(currentUserId, targetUserId))
                .requesterId(currentUserId)
                .status(FriendshipStatus.PENDING)
                .message(request.getMessage())
                .build();
        friendShip = friendShipRepository.save(friendShip);

        iNotificationService.sendFriendRequestNotification(targetUserId, currentUserId, friendShip.getId());

        return FriendShipResponse.toFriendShipResponse(friendShip, currentUserId);

    }

    @Override
    public FriendShipResponse respondToFriendRequest(RespondFriendRequest request) {
        Long currentUserId = getCurrentUserId();
        FriendShip friendShip = friendShipRepository.findById(request.getFriendshipId())
                .orElseThrow(() -> new AppException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        //Kiểm tra currentUser có thuộc friendship này không
        boolean isPartOfFriendship = friendShip.getUserAId().equals(currentUserId) 
                                || friendShip.getUserBId().equals(currentUserId);
        if (!isPartOfFriendship) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        //ng gui kh the tu chap nhan kb
        if(friendShip.getRequesterId().equals(currentUserId)){
            throw new AppException(ErrorCode.UN_RESPOND_REQUEST);
        }

        //chỉ respond được khi status = PENDING
        if(friendShip.getStatus() != FriendshipStatus.PENDING){
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        //update status
        friendShip.setStatus(request.getAccept() ? FriendshipStatus.ACCEPTED : FriendshipStatus.REJECTED);
        friendShipRepository.save(friendShip);

        if (request.getAccept()) {
            iNotificationService.sendFriendAcceptedNotification(
                    friendShip.getRequesterId(),
                    currentUserId,
                    friendShip.getId()
            );
        }

        return FriendShipResponse.toFriendShipResponse(friendShip, currentUserId);


    }

    @Override
    public List<FriendShipResponse> getFriendLists() {
        Long currentUserId = getCurrentUserId();
        List<FriendShip> friendShips = friendShipRepository.findByUserIdAndStatus(currentUserId, FriendshipStatus.ACCEPTED);
        List<FriendShipResponse> response = friendShips.stream()
                .map(fs -> mapToResponseWithFriendInfo(fs, currentUserId))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<FriendShipResponse> getPendingRequest() {
        Long currentUserId = getCurrentUserId();

        List<FriendShip> pending = friendShipRepository.findPendingRequestsForUser(currentUserId);
        List<FriendShipResponse> response = pending.stream()
                .map(fs -> {
                    FriendShipResponse resp = FriendShipResponse.toFriendShipResponse(fs, currentUserId);

                    userRepository.findById(fs.getRequesterId()).ifPresent(requester -> {
                        resp.setFriendUsername(requester.getUsername());
                        resp.setFriendDisplayName(requester.getDisplayName());
                        resp.setFriendAvatarUrl(requester.getAvatarUrl());
                    });

                    return resp;
                }).toList();
        return response;
    }

    @Override
    public List<FriendShipResponse> getSentRequest() {
        Long currentUserId = getCurrentUserId();

        List<FriendShip> sent = friendShipRepository.findSentRequests(currentUserId);
        return sent.stream()
                .map(fs -> {
                    FriendShipResponse resp = FriendShipResponse.toFriendShipResponse(fs, currentUserId);
                    Long targetId = resp.getFriendId();
                    userRepository.findById(targetId).ifPresent(target -> {
                        resp.setFriendUsername(target.getUsername());
                        resp.setFriendDisplayName(target.getDisplayName());
                        resp.setFriendAvatarUrl(target.getAvatarUrl());
                    });

                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void unFriend(Long friendshipId) {
        Long currentUserId = getCurrentUserId();

        FriendShip friendShip = friendShipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (!friendShip.getUserAId().equals(currentUserId) &&
                !friendShip.getUserBId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        friendShipRepository.delete(friendShip);
    }

    @Override
    public FriendShipResponse block(Long targetUserId) {
        Long currentUserId = getCurrentUserId();

        if (currentUserId.equals(targetUserId)) {
            throw new AppException(ErrorCode.CANNOT_BLOCK_YOURSELF);
        }
        FriendShip friendship = friendShipRepository.findByUserPair(currentUserId, targetUserId)
                .orElseGet(() -> FriendShip.builder()
                        .userAId(Math.min(currentUserId, targetUserId))
                        .userBId(Math.max(currentUserId, targetUserId))
                        .requesterId(currentUserId)
                        .build());

        friendship.setStatus(FriendshipStatus.BLOCKED);
        friendship = friendShipRepository.save(friendship);

        return FriendShipResponse.toFriendShipResponse(friendship, currentUserId);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaim("userId");
    }

    private FriendShipResponse mapToResponseWithFriendInfo(FriendShip fs, Long currentUserId) {
        FriendShipResponse resp = FriendShipResponse.toFriendShipResponse(fs, currentUserId);

        Long friendId = resp.getFriendId();

        userRepository.findById(friendId).ifPresent(friend -> {
            resp.setFriendUsername(friend.getUsername());
            resp.setFriendDisplayName(friend.getDisplayName());
            resp.setFriendAvatarUrl(friend.getAvatarUrl());
        });
        return resp;
    }
}
