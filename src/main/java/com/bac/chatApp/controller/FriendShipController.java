package com.bac.chatApp.controller;

import com.bac.chatApp.dto.request.friend.FriendRequest;
import com.bac.chatApp.dto.request.friend.RespondFriendRequest;
import com.bac.chatApp.dto.response.ApiResponse;
import com.bac.chatApp.dto.response.friend.FriendShipResponse;
import com.bac.chatApp.service.IFriendShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendShipController {

    private final IFriendShipService iFriendShipService;

    //gui kb
    @PostMapping("/request")
    public ApiResponse<FriendShipResponse> sendRequest(@RequestBody @Valid FriendRequest request){
        return ApiResponse.<FriendShipResponse>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.sendRequest(request))
                .build();
    }

    //chap nhan hoac tu choi
    @PostMapping("/respond")
    public ApiResponse<FriendShipResponse> respondToRequest(@RequestBody @Valid RespondFriendRequest request){
        return ApiResponse.<FriendShipResponse>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.respondToFriendRequest(request))
                .build();
    }

    //ds ban be
    @GetMapping("/friends")
    public ApiResponse<List<FriendShipResponse>> getFriendLists(){
        return ApiResponse.<List<FriendShipResponse>>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.getFriendLists())
                .build();
    }

    //ds da nhan kb
    @GetMapping("/pending")
    public ApiResponse<List<FriendShipResponse>> getPendingRequests() {
        return ApiResponse.<List<FriendShipResponse>>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.getPendingRequest())
                .build();
    }


    //ds da gui
    @GetMapping("/sent")
    public ApiResponse<List<FriendShipResponse>> getSentRequests() {
        return ApiResponse.<List<FriendShipResponse>>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.getSentRequest())
                .build();
    }
    //huy kb
    @DeleteMapping("/{friendshipId}")
    public ApiResponse<Void> unFriend(@PathVariable("friendshipId") Long friendshipId){
        iFriendShipService.unFriend(friendshipId);
        return ApiResponse.<Void>builder()
                .message("unfriend successfully")
                .build();
    }

    //block
    @PostMapping("/block/{targetUserId}")
    public ApiResponse<FriendShipResponse> blockUser(@PathVariable("targetUserId") Long targetUserId){
        return ApiResponse.<FriendShipResponse>builder()
                .errorCode(0)
                .message("")
                .data(iFriendShipService.block(targetUserId))
                .build();
    }
}
