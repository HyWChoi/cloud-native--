package com.finance.user_service.domain.profile.service;

import com.finance.user_service.domain.profile.dto.LoginRequest;
import com.finance.user_service.domain.profile.dto.ProfileResponse;
import com.finance.user_service.domain.profile.dto.RegisterRequest;
import com.finance.user_service.domain.profile.entity.Profile;
import com.finance.user_service.domain.profile.repository.ProfileRepository;
import com.finance.user_service.infraStructure.common.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponse register(RegisterRequest request) {
        // 이메일 중복 검사
        if (profileRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("이미 존재하는 이메일입니다.");
        }

        // 사용자 생성
        Profile profile = Profile.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .alias(request.getAlias())
                .build();

        Profile savedProfile = profileRepository.save(profile);
        return ProfileResponse.from(savedProfile);
    }

    public String login(LoginRequest request) {
        // 사용자 조회
        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.");
        }

        // 세션 생성 및 Redis 저장
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue()
                .set("session:" + sessionId, profile.getId().toString(), 2, TimeUnit.HOURS);

        return sessionId;
    }

    public void logout(String sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }

    public ProfileResponse getProfile(String sessionId) {
        String profileId = redisTemplate.opsForValue().get("session:" + sessionId);
        if (profileId == null) {
            throw new BusinessException("세션이 만료되었습니다.");
        }

        Profile profile = profileRepository.findById(Long.parseLong(profileId))
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다."));

        return ProfileResponse.from(profile);
    }
}
