package com.sparta.upgradeschedulemanagement.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class CommentRequestDto {
    private Long userId;
    private String content;
}
