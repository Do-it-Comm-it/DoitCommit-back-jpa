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

        String jwtToken = jwtUtil.generateAccessToken(principalDetails.getMember().getMemberId());

        //쿠키에 jwt 토큰 저장
        Cookie myCookie = new Cookie("Authentication", jwtToken);
        myCookie.setMaxAge(300);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);

        //redirect
        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }
}
