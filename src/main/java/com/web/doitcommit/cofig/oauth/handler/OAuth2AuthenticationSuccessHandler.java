package com.web.doitcommit.cofig.oauth.handler;

import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        System.out.println("성공!!!!!!!");

        Cookie myCookie = new Cookie("cookieName", "dlguddn");
        myCookie.setMaxAge(300);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);
        getRedirectStrategy().sendRedirect(request,response,"http://localhost:3000?guddn=123");
    }
}
