package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import com.web.doitcommit.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisService redisService;

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> verifyRefreshToken(@CookieValue("refreshToken") String refreshToken,
                                                        HttpServletResponse response){

        //refreshToken 검증
        Long memberId = jwtUtil.validateAndExtract(refreshToken);

        //잘못된 refreshToken 일 경우
        if (memberId == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //redis 에 등록된 refreshToken 가져오기
        String findRefreshToken = redisService.getValues(memberId);

        //잘못된 refreshToken 일 경우
        if(findRefreshToken == null || !findRefreshToken.equals(refreshToken) || refreshToken == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //새로운 accessToken, refreshToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);

        redisService.setValues(memberId, newRefreshToken);

        //쿠키에 accessToken, refreshToken 저장
        cookieUtil.createCookie(response, jwtUtil.accessTokenName, newAccessToken, jwtUtil.accessTokenExpire);
        cookieUtil.createCookie(response, jwtUtil.refreshTokenName, newRefreshToken,jwtUtil.refreshTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(1,"토큰 재발급 완료", null), HttpStatus.OK);
    }

}
