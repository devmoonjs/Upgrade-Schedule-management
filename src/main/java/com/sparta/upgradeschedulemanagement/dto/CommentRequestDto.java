package com.sparta.upgradeschedulemanagement.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentRequestDto {
    private final String author;
    private final String content;
}
