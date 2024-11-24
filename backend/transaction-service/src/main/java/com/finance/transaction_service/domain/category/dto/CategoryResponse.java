package com.finance.transaction_service.domain.category.dto;

import com.finance.transaction_service.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .content(category.getContent())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
