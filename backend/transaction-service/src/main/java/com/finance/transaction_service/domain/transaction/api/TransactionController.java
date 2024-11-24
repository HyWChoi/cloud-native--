package com.finance.transaction_service.domain.transaction.api;

import com.finance.transaction_service.domain.SessionProfile;
import com.finance.transaction_service.domain.transaction.dto.TransactionCreateRequest;
import com.finance.transaction_service.domain.transaction.dto.TransactionListResponse;
import com.finance.transaction_service.domain.transaction.dto.TransactionResponse;
import com.finance.transaction_service.domain.transaction.dto.TransactionUpdateRequest;
import com.finance.transaction_service.domain.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "거래내역", description = "거래내역 관리 API")
@RequestMapping("/transaction_service/transactions/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "거래내역 생성", description = "새로운 거래내역을 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래내역 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(hidden = true) HttpServletRequest httpRequest,
            @RequestBody TransactionCreateRequest request
    ) {
        SessionProfile profile = (SessionProfile) httpRequest.getAttribute("SESSION_PROFILE");
        return ResponseEntity.ok(transactionService.createTransaction(profile.getProfileId(), request));
    }

    @Operation(summary = "거래내역 조회", description = "특정 거래내역의 상세 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래내역 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "거래내역을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        // 트랜잭션 소유자 검증 로직 추가
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @Operation(summary = "사용자 거래내역 목록 조회", description = "현재 로그인한 사용자의 모든 거래내역을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래내역 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/profile")
    public ResponseEntity<TransactionListResponse> getTransactionsByProfileId(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        SessionProfile profile = (SessionProfile) request.getAttribute("SESSION_PROFILE");
        return ResponseEntity.ok(
                transactionService.getTransactionsByProfileId(profile.getProfileId())
        );
    }

    @Operation(summary = "거래내역 수정", description = "기존 거래내역의 정보를 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래내역 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "거래내역을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionUpdateRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    ) {
        // 트랜잭션 소유자 검증 로직 추가
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }

    @Operation(summary = "거래내역 삭제", description = "지정된 거래내역을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래내역 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "거래내역을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        // 트랜잭션 소유자 검증 로직 추가
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}