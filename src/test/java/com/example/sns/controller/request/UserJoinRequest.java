package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
}
