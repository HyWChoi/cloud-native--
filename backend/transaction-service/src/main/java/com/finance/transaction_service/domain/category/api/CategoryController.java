package com.finance.transaction_service.domain.category.api;

import com.finance.transaction_service.domain.category.dto.CategoryCreateRequest;
import com.finance.transaction_service.domain.category.dto.CategoryListResponse;
import com.finance.transaction_service.domain.category.dto.CategoryResponse;
import com.finance.transaction_service.domain.category.dto.CategoryUpdateRequest;
import com.finance.transaction_service.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "카테고리", description = "카테고리 관리 API")
@RequestMapping("/transaction_service/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 세션"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-USER-ID") Long profileId,
            @Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(profileId, request));
    }

    @Operation(summary = "카테고리 목록 조회", description = "사용자의 모든 카테고리 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 세션")
    })
    @GetMapping("/get")
    public ResponseEntity<CategoryListResponse> getCategories(
            @RequestHeader("X-USER-ID") Long profileId) {
        return ResponseEntity.ok(categoryService.getCategoriesByProfileId(profileId));
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리의 정보를 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 세션"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader("X-USER-ID") Long profileId,
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(profileId, categoryId, request));
    }

    @Operation(summary = "카테고리 삭제", description = "지정된 카테고리를 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 세션"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader("X-USER-ID") Long profileId,
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(profileId, categoryId);
        return ResponseEntity.noContent().build();
    }
}

