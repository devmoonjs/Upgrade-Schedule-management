package com.sparta.upgradeschedulemanagement.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserRequestDto {
    private String adminKey;
    private String name;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
