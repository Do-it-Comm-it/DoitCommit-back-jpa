package com.web.doitcommit.redis;

import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    private JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;

    /**
     * redis 저장 - {memberId: refreshToken}
     */
    public void setValues(Long memberId, String refreshToken){
        ValueOperations<Long, String> values = redisTemplate.opsForValue();
        values.set(memberId, refreshToken, Duration.ofMinutes(jwtUtil.refreshTokenExpire));  // 2주뒤 메모리에서 삭제된다.
    }

    /**
     * redis 조회
     */
    public String getValues(Long memberId){
        ValueOperations<Long, String> values = redisTemplate.opsForValue();
        return values.get(memberId);
    }

    /**
     * redies 삭제
     */
    public void delValues(Long memberId) {
        redisTemplate.delete(memberId);
    }
}
