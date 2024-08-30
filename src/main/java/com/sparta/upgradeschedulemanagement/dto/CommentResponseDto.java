package com.sparta.upgradeschedulemanagement.dto;

import com.sparta.upgradeschedulemanagement.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Long todoId;

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getTodo().getTodoId()
        );
    }
}