package com.finance.transaction_service.domain.transaction.repository;

import com.finance.transaction_service.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.profileId = :profileId AND t.deletedAt IS NULL")
    List<Transaction> findAllByProfileId(Long profileId);

    @Query("SELECT t FROM Transaction t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Transaction> findByIdAndDeletedAtIsNull(Long id);
}