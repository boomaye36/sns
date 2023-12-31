package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "password invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "token is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "post not found"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "permission invalid"),
    ALREADY_LIKED(HttpStatus.CONFLICT, "user already liked post"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "comment not found"),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connecting alarm occur error");
    private HttpStatus status;
    private String message;
}
