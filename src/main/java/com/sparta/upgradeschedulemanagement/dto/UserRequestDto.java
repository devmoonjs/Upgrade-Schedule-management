package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserRequestDto {
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
