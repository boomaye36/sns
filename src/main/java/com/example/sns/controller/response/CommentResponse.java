package com.example.sns.controller.response;

import com.example.sns.model.Comment;
import com.example.sns.model.Post;
import com.example.sns.model.User;
import com.example.sns.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;

    private String comment;

    private UserResponse user;

    private PostResponse post;

    private CommentResponse parentComment;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static CommentResponse fromComment(Comment comment) {
        if(comment == null) return null;
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                UserResponse.fromUser(comment.getUser()),
                PostResponse.froPost(comment.getPost()),
                CommentResponse.fromComment(comment.getParentComment()),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getRemovedAt()
        );
    }
}
