package com.web.doitcommit.redis;

import com.web.doitcommit.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisServiceTest {

    @Autowired RedisService redisService;
    @Autowired RedisTemplate redisTemplate;
    @Autowired JwtUtil jwtUtil;

    /**
     * 저장
     */
    @Test
    void save() throws Exception{
        //given
        Long memberId = 1L;
        String refreshToken = jwtUtil.generateRefreshToken(1L);

        //when
        redisService.setValues(memberId,refreshToken);

        //then
        String findToken = (String)redisTemplate.opsForValue().get(memberId);
        assertThat(findToken).isEqualTo(refreshToken);

    }

    /**
     * 조회
     */
    @Test
    void read() throws Exception{
        //given
        Long memberId = 1L;
        String refreshToken = jwtUtil.generateRefreshToken(1L);

        redisTemplate.opsForValue().set(1L,refreshToken);

        //when
        String findToken = redisService.getValues(1L);

        //then
        assertThat(findToken).isEqualTo(refreshToken);
    }
    
    /**
     * 갱신
     */
    @Test
    void update() throws Exception{
        //given
        IntStream.rangeClosed(1,5).forEach(i->{
            String refreshToken = jwtUtil.generateRefreshToken((long) i);
            ValueOperations<Long, String> values = redisTemplate.opsForValue();
            values.set((long)i, refreshToken);
        });

        //새로운 refreshToken
        String newRefreshToken = jwtUtil.generateRefreshToken(2L);

        //when
        redisService.setValues(2L, newRefreshToken);

        //then
        String findToken = (String)redisTemplate.opsForValue().get(2L);
        assertThat(findToken).isEqualTo(newRefreshToken);
    }
    
    

    /**
     * 삭제
     */
    @Test
    void delete() throws Exception{
        //given
        IntStream.rangeClosed(1,5).forEach(i->{
            String refreshToken = jwtUtil.generateRefreshToken((long) i);
            ValueOperations<Long, String> values = redisTemplate.opsForValue();
            values.set((long)i, refreshToken);
        });

        //when
        redisService.delValues(1L);

        //then
        assertThat(redisTemplate.opsForValue().get(1L)).isNull();

    }
    


}