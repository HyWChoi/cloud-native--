package com.finance.transaction_service.domain.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionListResponse {
    private List<TransactionResponse> transactions;
    private long totalCount;
}
