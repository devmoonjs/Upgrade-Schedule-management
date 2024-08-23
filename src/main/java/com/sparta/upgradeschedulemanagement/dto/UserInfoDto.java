package com.sparta.upgradeschedulemanagement.dto;

import com.sparta.upgradeschedulemanagement.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoDto {
    private final Long userId;
    private final String name;
    private final String email;

    public static UserInfoDto of(User user) {
        return new UserInfoDto(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
