package com.example.sns.controller.request;


import lombok.*;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String name;
    private String password;

    public UserJoinRequest() {
    }
}
