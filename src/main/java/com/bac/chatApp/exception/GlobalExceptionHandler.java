package com.bac.chatApp.exception;

import com.bac.chatApp.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    //handle app exception
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setErrorCode(errorCode.getErrorCode());
        apiResponse.setMessage(errorCode.getErrorMessage());
        apiResponse.setData(null);
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setErrorCode(errorCode.getErrorCode());
        apiResponse.setMessage(errorCode.getErrorMessage());
        apiResponse.setData(null);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
