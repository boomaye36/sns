package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.AlarmArgument;
import com.example.sns.model.AlarmType;
import com.example.sns.model.Comment;
import com.example.sns.model.Post;
import com.example.sns.model.entity.*;
import com.example.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final AlarmService alarmService;

    @Transactional
    public void create(String title, String body, String userName){
        //user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
        // post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

        if (postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with $s", userName, postId));
        }
        postEntity.setTitle(title);
        postEntity.setBody(body);
        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", userName, postId));
        }
        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }
    public Page<Post> my(String userName, Pageable pageable){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }


    @Transactional
    public void like(Integer postId, String userName) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

        // check liked -> throw

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED,
                    String.format("username %s already like post %d", userName, postId));
        });
        // likeEntityRepository

        // like save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST,
                new AlarmArgument(userEntity.getId(), postEntity.getId()))); // post를 작성한 사람에게 알람전송
        alarmService.send(alarmEntity.getId(), postEntity.getUser().getId()); // sse 알람전송
    }

    public Integer likeCnt(Integer postId) {

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

        // check liked -> throw

       Integer likeCnt = likeEntityRepository.countByPost(postEntity);
       return likeCnt;
    }

    @Transactional
    public void createComment(String comment, Integer postId, String userName){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
        commentEntityRepository.save(CommentEntity.of(comment, postEntity, userEntity));
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST,
                new AlarmArgument(userEntity.getId(), postEntity.getId()))); // post를 작성한 사람에게 알람전송
        alarmService.send(alarmEntity.getId(), postEntity.getUser().getId());
    }

    @Transactional
    public void createReply(String comment, Integer parentId, Integer postId, String userName){
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

            CommentEntity parentComment = commentEntityRepository.findById(parentId)
                    .orElseThrow(() -> new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND));
            CommentEntity replyComment = CommentEntity.of(comment, postEntity, userEntity);
            replyComment.setParentComment(parentComment);
            commentEntityRepository.save(replyComment);

    }

    public Page<Comment> getComment(Integer postId, Pageable pageable) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
//        Page<Comment> comments = commentEntityRepository.findAllByPostId(postId, pageable).map(Comment::fromEntity);
//        System.out.println(comments.stream().map(Comment::getComment));
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }
}
