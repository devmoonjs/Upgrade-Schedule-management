package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodoUserResponseDto {
    private final Long userId;
    private final String name;
    private final String email;
    private final String title;
    private final String content;
}
