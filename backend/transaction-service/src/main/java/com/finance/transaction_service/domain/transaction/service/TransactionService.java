package com.finance.transaction_service.domain.transaction.service;

import com.finance.transaction_service.domain.SessionProfile;
import com.finance.transaction_service.domain.category.dto.CategoryResponse;
import com.finance.transaction_service.domain.category.entity.Category;
import com.finance.transaction_service.domain.category.repository.CategoryRepository;
import com.finance.transaction_service.domain.transaction.dto.TransactionCreateRequest;
import com.finance.transaction_service.domain.transaction.dto.TransactionListResponse;
import com.finance.transaction_service.domain.transaction.dto.TransactionResponse;
import com.finance.transaction_service.domain.transaction.dto.TransactionUpdateRequest;
import com.finance.transaction_service.domain.transaction.entity.Transaction;
import com.finance.transaction_service.domain.transaction.entity.TransactionCategory;
import com.finance.transaction_service.domain.transaction.repository.TransactionCategoryRepository;
import com.finance.transaction_service.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, SessionProfile> redisTemplate;

    @Transactional
    public TransactionResponse createTransaction(Long profileId, TransactionCreateRequest request) {

        Transaction transaction = Transaction.builder()
                .profileId(profileId)
                .transactionType(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        List<TransactionCategory> transactionCategories = request.getCategoryIds().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    return TransactionCategory.builder()
                            .transaction(savedTransaction)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        transactionCategoryRepository.saveAll(transactionCategories);

        return buildTransactionResponse(savedTransaction, transactionCategories);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        List<TransactionCategory> transactionCategories =
                transactionCategoryRepository.findAllByTransactionId(transaction.getId());

        return buildTransactionResponse(transaction, transactionCategories);
    }

    @Transactional(readOnly = true)
    public TransactionListResponse getTransactionsByProfileId(Long profileId) {
        List<Transaction> transactions = transactionRepository.findAllByProfileId(profileId);

        List<TransactionResponse> responses = transactions.stream()
                .map(transaction -> {
                    List<TransactionCategory> categories =
                            transactionCategoryRepository.findAllByTransactionId(transaction.getId());
                    return buildTransactionResponse(transaction, categories);
                })
                .collect(Collectors.toList());

        return TransactionListResponse.builder()
                .transactions(responses)
                .totalCount(responses.size())
                .build();
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionCategoryRepository.deleteAllByTransactionId(id);
        transactionCategoryRepository.flush();

        Transaction updatedTransaction = Transaction.builder()
                .id(transaction.getId())
                .profileId(transaction.getProfileId())
                .transactionType(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .createdAt(transaction.getCreatedAt())
                .build();

        transactionRepository.save(updatedTransaction);
        transactionRepository.flush();

        // Create and save new categories one by one to avoid batch insert issues
        List<TransactionCategory> newCategories = new ArrayList<>();
        for (Long categoryId : request.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            TransactionCategory transactionCategory = TransactionCategory.builder()
                    .transaction(updatedTransaction)
                    .category(category)
                    .build();
            newCategories.add(transactionCategoryRepository.save(transactionCategory));
        }

        return buildTransactionResponse(updatedTransaction, newCategories);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction = Transaction.builder()
                .id(transaction.getId())
                .profileId(transaction.getProfileId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .deletedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    private TransactionResponse buildTransactionResponse(
            Transaction transaction,
            List<TransactionCategory> transactionCategories
    ) {
        List<CategoryResponse> categoryResponses = transactionCategories.stream()
                .map(tc -> CategoryResponse.builder()
                        .id(tc.getCategory().getId())
                        .content(tc.getCategory().getContent())
                        .build())
                .collect(Collectors.toList());

        return TransactionResponse.builder()
                .id(transaction.getId())
                .profileId(transaction.getProfileId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .categories(categoryResponses)
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}