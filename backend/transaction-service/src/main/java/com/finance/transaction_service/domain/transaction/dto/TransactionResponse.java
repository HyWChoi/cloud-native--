package com.finance.transaction_service.domain.transaction.dto;

import com.finance.transaction_service.domain.category.dto.CategoryResponse;
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
public class TransactionResponse {
    private Long id;
    private Long profileId;
    private String transactionType;
    private Long amount;
    private String description;
    private LocalDateTime date;
    private List<CategoryResponse> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
