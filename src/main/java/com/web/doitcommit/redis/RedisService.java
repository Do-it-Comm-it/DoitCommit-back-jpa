package com.web.doitcommit.redis;

import com.web.doitcommit.handler.exception.CustomException;
import com.web.doitcommit.handler.exception.NoSavedDataException;
import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

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
     * redis 삭제
     */
    public void delValues(Long memberId) {
        redisTemplate.delete(memberId);
    }

    /**
     * redis 저장 - {날짜 : popularTagList}
     */
    public void setList(List<Object[]> popularTagList){
        ListOperations<String, List<Object[]>> listOperations = redisTemplate.opsForList();
        listOperations.rightPushAll(LocalDate.now().toString(), popularTagList);

    }

    /**
     * redis 날짜별 인기태그 조회
     */
    public List<Object[]> getValues(String date){
        ListOperations<String, List<Object[]>> listOperations = redisTemplate.opsForList();

        try{
           return listOperations.range(date, 0, 1).get(0);
        }catch (IndexOutOfBoundsException e){
            throw new NoSavedDataException("저장된 인기태그가 없습니다.");
        }
    }

    /**
     * redis 날짜별 인기태그 삭제
     */
    public void delPopularTag(String date){
        redisTemplate.delete(date);
    }
}
