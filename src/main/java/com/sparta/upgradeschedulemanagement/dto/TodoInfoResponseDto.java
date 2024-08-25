package com.sparta.upgradeschedulemanagement.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TodoInfoResponseDto {
    private String title;
    private String content;
    private List<UserInfoDto> users;
}
