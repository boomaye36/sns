package com.example.sns.repository;

import com.example.sns.model.Post;
import com.example.sns.model.entity.LikeEntity;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    Integer countByPost(PostEntity post);

    @Transactional
    @Query("UPDATE LikeEntity entity SET deleted_at = NOW() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
