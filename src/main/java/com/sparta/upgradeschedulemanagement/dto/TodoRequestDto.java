package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;

@Getter
public class TodoRequestDto {
    private Long userId;
    private String title;
    private String content;
}
