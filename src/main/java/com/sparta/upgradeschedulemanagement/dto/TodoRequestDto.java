package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoRequestDto {
    private final String author;
    private final String title;
    private final String content;
}
