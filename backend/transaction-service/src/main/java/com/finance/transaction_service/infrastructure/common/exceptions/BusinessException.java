package com.finance.transaction_service.infrastructure.common.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}