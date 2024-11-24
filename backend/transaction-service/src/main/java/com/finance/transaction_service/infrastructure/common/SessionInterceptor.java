package com.finance.transaction_service.infrastructure.common;


import com.finance.transaction_service.domain.SessionProfile;
import com.finance.transaction_service.infrastructure.common.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, SessionProfile> sessionRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("X-USER-ID");
        if (userId == null) {
            throw new BusinessException("세션이 없습니다.");
        }

        // 직접 SessionProfile 생성
        SessionProfile profile = new SessionProfile(
                Long.parseLong(userId),
                null,  // email
                null,  // name
                "USER" // default role
        );

        request.setAttribute("SESSION_PROFILE", profile);
        return true;
    }
}
