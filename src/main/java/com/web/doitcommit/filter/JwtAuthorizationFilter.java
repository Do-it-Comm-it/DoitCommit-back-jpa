package com.web.doitcommit.filter;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern = "/auth/**/*";
    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;
    private MemberRepository memberRepository;

    @Autowired
    public JwtAuthorizationFilter(CookieUtil cookieUtil, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.antPathMatcher = new AntPathMatcher();
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //인증이 필요 없는 uri 일 경우 바로 통과
        if (antPathMatcher.match(pattern, request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("인증필터 시작");

        Cookie cookie = cookieUtil.getCookie(request, jwtUtil.accessTokenName);
        //accessToken 쿠키가 존재할 경우
        if (cookie != null) {
            String accessToken = cookie.getValue();
            Long memberId = jwtUtil.validateAndExtract(accessToken);

            //accessToken 이 유효할 경우
            if (memberId != null) {
                System.out.println("memberId: " + memberId);
                Member member = memberRepository.findById(memberId).orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않은 회원입니다."));

                PrincipalDetails principalDetails = new PrincipalDetails(member);

                //Authentication 객체 만들기
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                //시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        chain.doFilter(request, response);
    }

}
