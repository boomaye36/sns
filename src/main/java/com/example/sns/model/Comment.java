package com.example.sns.model;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
@Getter
@AllArgsConstructor
public class Comment {

    private Integer id;

    private String comment;

    private User user;

    private Post post;

    private Comment parentComment;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static Comment fromEntity(CommentEntity entity){
        Comment parentComment = entity.getParentComment() != null ?
                Comment.fromEntity(entity.getParentComment()) : null;
        return new Comment(
                entity.getId(),
                entity.getComment(),
                User.fromEntity(entity.getUser()),
                Post.fromEntity(entity.getPost()),
                parentComment,
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

}
