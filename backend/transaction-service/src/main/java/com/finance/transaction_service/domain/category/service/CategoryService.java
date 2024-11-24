package com.finance.transaction_service.domain.category.service;

import com.finance.transaction_service.domain.category.dto.CategoryCreateRequest;
import com.finance.transaction_service.domain.category.dto.CategoryListResponse;
import com.finance.transaction_service.domain.category.dto.CategoryResponse;
import com.finance.transaction_service.domain.category.dto.CategoryUpdateRequest;
import com.finance.transaction_service.domain.category.entity.Category;
import com.finance.transaction_service.domain.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(Long profileId, CategoryCreateRequest request) {
        Category category = Category.builder()
                .profileId(profileId)
                .content(request.getContent())
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryListResponse getCategoriesByProfileId(Long profileId) {
        List<Category> categories = categoryRepository.findByProfileIdAndDeletedAtIsNull(profileId);
        return CategoryListResponse.of(categories);
    }

    @Transactional
    public CategoryResponse updateCategory(Long profileId, Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findByIdAndProfileId(categoryId, profileId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setContent(request.getContent());
        categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    @Transactional
    public void deleteCategory(Long profileId, Long categoryId) {
        Category category = categoryRepository.findByIdAndProfileId(categoryId, profileId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
