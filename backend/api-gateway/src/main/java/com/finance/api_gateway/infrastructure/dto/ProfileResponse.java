package com.finance.api_gateway.infrastructure.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String email;
    private String alias;
}