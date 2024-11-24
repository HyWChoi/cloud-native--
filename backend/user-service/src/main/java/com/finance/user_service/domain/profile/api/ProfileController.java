package com.finance.user_service.domain.profile.api;

import com.finance.user_service.domain.profile.dto.LoginRequest;
import com.finance.user_service.domain.profile.dto.LoginResponse;
import com.finance.user_service.domain.profile.dto.ProfileResponse;
import com.finance.user_service.domain.profile.dto.RegisterRequest;
import com.finance.user_service.domain.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/user_service/profile")
@AllArgsConstructor
@Tag(name = "User Profile", description = "사용자 프로필 관리 API")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
    })
    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        ProfileResponse response = profileService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "사용자 인증 후 세션 ID를 반환합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        String sessionId = profileService.login(request);
        return LoginResponse.builder().sessionId(sessionId).build();
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 종료합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 세션")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "세션 ID", required = true)
            @RequestHeader("X-Session-ID") String sessionId
    ) {
        profileService.logout(sessionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 세션"),
            @ApiResponse(responseCode = "404", description = "프로필을 찾을 수 없음")
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile(
            @Parameter(description = "세션 ID", required = true)
            @RequestHeader("X-Session-ID") String sessionId
    ) {
        ProfileResponse response = profileService.getProfile(sessionId);
        return ResponseEntity.ok(response);
    }
}
