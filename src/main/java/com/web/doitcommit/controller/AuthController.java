package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
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
    private final RedisService redisService;

    //TODO uri 설계, response 객체 의논 필요
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

        //쿠키에 accessToken, refreshToken 저장
        setCookie(response,"accessToken", newAccessToken);
        setCookie(response,"refreshToken", newRefreshToken);

        return new ResponseEntity<>(new CMRespDto<>(1,"토큰 재발급 완료", null), HttpStatus.OK);
    }

    private void setCookie(HttpServletResponse response, String name, String jwtToken) {

        Cookie cookie = new Cookie(name, jwtToken);
        cookie.setMaxAge(300); // 모든 경로에서 접근 가능 하도록 설정
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }

}
