package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
