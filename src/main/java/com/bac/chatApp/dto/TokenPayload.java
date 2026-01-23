package com.bac.chatApp.dto;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenPayload {
    private String token;
    private String jwtId;
    private Date expiredTime;
}
