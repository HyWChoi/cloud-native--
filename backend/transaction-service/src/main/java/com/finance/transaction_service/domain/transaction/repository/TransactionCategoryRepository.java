package com.finance.transaction_service.domain.transaction.repository;

import com.finance.transaction_service.domain.transaction.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    List<TransactionCategory> findAllByTransactionId(Long transactionId);
    void deleteAllByTransactionId(Long transactionId);
}