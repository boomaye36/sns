package com.example.sns.fixture;

import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.security.core.parameters.P;

public class PostEntityFixture {
    public static PostEntity get(String userName, Integer userId, Integer postId){
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        return result;
    }
}
