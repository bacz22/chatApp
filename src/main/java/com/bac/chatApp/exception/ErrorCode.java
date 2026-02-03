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

    UNAUTHORIZED(9999, "bạn không có quyền thực hiện chức năng này", HttpStatus.UNAUTHORIZED),

    //user 1000-1999
    USER_EXISTED(1000,"người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1001,"email đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1002, "không tìm thấy người dùng", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCHES(1003, "mật khẩu không khớp", HttpStatus.BAD_REQUEST),
    FILE_IS_NULL(1004, "file không tồn tại", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1005, "file quá lớn", HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT(1006, "file không đúng định dạng", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1007, "upload thất bại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1008, "username phải có ít nhất 5 kí tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1009, "mật khấu phải có ít nhất 6 kí tự", HttpStatus.BAD_REQUEST),
    EMAIL_INCORRECT(1010, "email không đúng định dạng", HttpStatus.BAD_REQUEST),
    DISPLAY_NAME_NOT_BLANK(1011, "tên hiển thị không được để trống", HttpStatus.BAD_REQUEST),
    //auth 2000-2999
    TOKEN_INVALID(2000, "token không hợp lệ", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(2001, "refresh token hết hạn", HttpStatus.UNAUTHORIZED),

    //friendship 3000-3999
    CANNOT_FRIEND_YOURSELF(3000, "không thể tự gửi kết bạn cho chính mình", HttpStatus.BAD_REQUEST),
    ALREADY_FRIENDS(3001, "hiện tại đã là bạn bè với người dùng này", HttpStatus.BAD_REQUEST),
    REQUEST_ALREADY_SENT(3002, "đã gửi kết bạn cho người dùng này", HttpStatus.BAD_REQUEST),
    USER_BLOCKED(3003, "bạn đã bị chặn", HttpStatus.BAD_REQUEST),
    FRIENDSHIP_NOT_FOUND(3004, "không tìm thấy lời mời/quan hệ bạn bè", HttpStatus.BAD_REQUEST),
    UN_RESPOND_REQUEST(3005, "người gửi không thể tự chấp nhận mời mời kết bạn đã gửi",HttpStatus.BAD_REQUEST),
    REQUEST_ALREADY_PROCESSED(3006, "Chỉ có thể phản hồi các yêu cầu đang chờ xử lý.", HttpStatus.BAD_REQUEST),
    CANNOT_BLOCK_YOURSELF(3007, "không thể tự chặn chính mình", HttpStatus.BAD_REQUEST),

    //notification 4000-4999
    NOTIFICATION_NOT_FOUND(4000, "Không tìm thấy thông báo này", HttpStatus.BAD_REQUEST),
    ;
    private int errorCode;
    private String errorMessage;
    private HttpStatusCode statusCode;
}
