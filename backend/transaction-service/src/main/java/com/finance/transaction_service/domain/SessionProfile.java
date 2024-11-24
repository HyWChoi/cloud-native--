package com.finance.transaction_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionProfile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long profileId;
    private String email;
    private String name;
    private String role;

    // 필수 필드만 있는 생성자 추가
    public SessionProfile(Long profileId) {
        this.profileId = profileId;
        this.role = "USER";
    }
}