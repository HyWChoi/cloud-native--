package com.finance.user_service.domain.profile.dto;

import com.finance.user_service.domain.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private Long id;
    private String email;
    private String alias;

    public static ProfileResponse from(Profile user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .alias(user.getAlias())
                .build();
    }
}
