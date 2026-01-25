package com.bac.chatApp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    //user 1000-1999
    USER_EXISTED(1000,"user existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1001,"email existed", HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1002, "user not found", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCHES(1003, "password not matches", HttpStatus.BAD_REQUEST),
    FILE_IS_NULL(1004, "file is null", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1005, "file too large", HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT(1006, "invalid file format", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1007, "upload fail", HttpStatus.BAD_REQUEST),
    //auth 2000-2999
    TOKEN_INVALID(2000, "token invalid", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(2001, "refresh token expired", HttpStatus.UNAUTHORIZED),
    ;
    private int errorCode;
    private String errorMessage;
    private HttpStatusCode statusCode;
}
