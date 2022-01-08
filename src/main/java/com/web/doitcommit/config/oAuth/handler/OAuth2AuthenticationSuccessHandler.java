package com.web.doitcommit.config.oAuth.handler;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.jwt.JwtUtil;
import com.web.doitcommit.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private String redirectUrl = "http://localhost:3000";

    @Value("${app.token.accessTokenName}")
    private String accessTokenName;

    @Value("${app.token.refreshTokenName}")
    private String refreshTokenName;

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, RedisService redisService) {

        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws  IOException {
        System.out.println("성공!!!!!!!");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //accessToken, refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(principalDetails.getMember().getMemberId());
        String refreshToken = jwtUtil.generateRefreshToken(principalDetails.getMember().getMemberId());

        //refreshToken - redis 에 저장
        redisService.setValues(principalDetails.getMember().getMemberId(), refreshToken);

        //accessToken, refreshToken - response 쿠키에 저장
        setCookie(response, accessTokenName, accessToken);
        setCookie(response, refreshTokenName, refreshToken);

        //redirect
        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
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
