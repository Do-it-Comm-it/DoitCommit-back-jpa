package com.web.doitcommit.cofig.oAuth.handler;

import com.web.doitcommit.cofig.auth.PrincipalDetails;
import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorized-redirect-url}")
    private String redirectUrl;

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws  IOException {
        System.out.println("성공!!!!!!!");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //accessToken, refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(principalDetails.getMember().getMemberId());
        String refreshToken = jwtUtil.generateRefreshToken(principalDetails.getMember().getMemberId());

        //accessToken, refreshToken - response 쿠키에 저장
        setCookie(response, "accessToken", accessToken);
        setCookie(response, "refreshToken", refreshToken);

        //redirect
        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }

    private void setCookie(HttpServletResponse response, String name, String jwtToken) {

        Cookie cookie = new Cookie(name, jwtToken);
        cookie.setMaxAge(300); // 모든 경로에서 접근 가능 하도록 설정
        cookie.setPath("/");

        response.addCookie(cookie);
    }

}
