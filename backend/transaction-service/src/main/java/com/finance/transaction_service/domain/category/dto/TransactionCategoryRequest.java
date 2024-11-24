package com.finance.transaction_service.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCategoryRequest {
    @NotNull(message = "카테고리 ID를 입력해주세요")
    private List<Long> categoryIds;
}
