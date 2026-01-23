package com.bac.chatApp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("token")
public class RedisToken {
    @Id
    private String id;

    @Indexed
    private String jwtId;

    @Indexed
    private String userId;

    private String username;

    private String tokenType;

    @TimeToLive
    private Long ttl;
}
