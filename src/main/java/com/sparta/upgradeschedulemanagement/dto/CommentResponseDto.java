package com.sparta.upgradeschedulemanagement.dto;

import com.sparta.upgradeschedulemanagement.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long todoId;

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getTodo().getTodoId()
        );
    }
}