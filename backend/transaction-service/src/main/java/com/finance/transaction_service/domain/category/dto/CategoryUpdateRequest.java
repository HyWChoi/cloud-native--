package com.finance.transaction_service.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {
    @NotBlank(message = "카테고리 내용을 입력해주세요")
    private String content;
}
