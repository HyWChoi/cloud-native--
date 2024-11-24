package com.finance.transaction_service.domain.category.dto;

import com.finance.transaction_service.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListResponse {
    private List<CategoryResponse> categories;
    private int totalCount;

    public static CategoryListResponse of(List<Category> categories) {
        List<CategoryResponse> responses = categories.stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        return CategoryListResponse.builder()
                .categories(responses)
                .totalCount(responses.size())
                .build();
    }
}
