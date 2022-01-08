package com.web.doitcommit.controller;

import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import com.web.doitcommit.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    @Value("${app.token.accessTokenExpire}")
    private int accessTokenExpire;

    @Value("${app.token.refreshTokenExpire}")
    private int refreshTokenExpire;

    @Value("${app.token.accessTokenName}")
    private String accessTokenName;

    @Value("${app.token.refreshTokenName}")
    private String refreshTokenName;

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private CookieUtil cookieUtil;

    //TODO uri 설계, response 객체 의논 필요
//    @GetMapping("/auth/refreshToken")
//    public ResponseEntity<?> verifyRefreshToken(@CookieValue("refreshToken") String refreshToken,
//                                                        HttpServletResponse response){
//
//        Long memberId = jwtUtil.validateAndExtract(refreshToken);
//
//        if (memberId == null){
//            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
//        }
//        String findRefreshToken = redisService.getValues(memberId);
//
//        //잘못된 refreshToken 일 경우
//        if(findRefreshToken == null || !findRefreshToken.equals(refreshToken) || refreshToken == null){
//            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
//        }
//
//        //새로운 accessToken, refreshToken 생성
//        String newAccessToken = jwtUtil.generateAccessToken(memberId);
//        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);
//
//        System.out.println(newAccessToken);
//        System.out.println(newRefreshToken);
//        //쿠키에 accessToken, refreshToken 저장
//        setCookie(response,"accessToken", newAccessToken);
//        setCookie(response,"refreshToken", newRefreshToken);
//
//        return new ResponseEntity<>(new CMRespDto<>(1,"토큰 재발급 완료", null), HttpStatus.OK);
//    }

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> verifyRefreshToken(@CookieValue("${app.auth.refreshTokenName}") String refreshJwt,
                                                        HttpServletResponse response){


        Long memberId = jwtUtil.getMemberIdFromToken(refreshJwt);

        if (memberId == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }
        String redisRefreshToken = redisService.getValues(memberId);

        //잘못된 refreshToken 일 경우
        if(redisRefreshToken == null || !redisRefreshToken.equals(refreshJwt) || refreshJwt == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //새로운 accessToken, refreshToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);

        System.out.println(newAccessToken);
        System.out.println(newRefreshToken);
        //쿠키에 accessToken, refreshToken 저장
        cookieUtil.createCookie(response,accessTokenName, newAccessToken, accessTokenExpire);
        cookieUtil.createCookie(response,refreshTokenName, newRefreshToken, refreshTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(1,"토큰 재발급 완료", null), HttpStatus.OK);
    }

}
