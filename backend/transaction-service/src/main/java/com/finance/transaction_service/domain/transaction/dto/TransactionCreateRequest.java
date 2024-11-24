package com.finance.transaction_service.domain.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateRequest {
    @NotBlank(message = "거래 타입은 필수입니다")
    private String type;

    @NotNull(message = "카테고리 ID는 필수입니다")
    private List<Long> categoryIds;

    @NotBlank(message = "설명은 필수입니다")
    private String description;

    @NotNull(message = "금액은 필수입니다")
    @Positive(message = "금액은 양수여야 합니다")
    private Long amount;

    @NotNull(message = "날짜는 필수입니다")
    private LocalDateTime date;
}