package com.finance.user_service.infraStructure.common;

import com.finance.user_service.infraStructure.common.exceptions.BusinessException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @Nullable HttpServletResponse response,
                             @Nullable Object handler) {
        String sessionId = request.getHeader("X-Session-ID");
        if (sessionId == null) {
            throw new BusinessException("세션 ID가 없습니다.");
        }

        String userId = redisTemplate.opsForValue().get("session:" + sessionId);
        if (userId == null) {
            throw new BusinessException("세션이 만료되었습니다.");
        }

        return true;
    }
}
