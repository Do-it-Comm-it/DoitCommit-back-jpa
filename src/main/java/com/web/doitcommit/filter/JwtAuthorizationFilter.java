package com.web.doitcommit.filter;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.jwt.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;

    public JwtAuthorizationFilter(String pattern, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //인증이 필요 없는 uri 일 경우 바로 통과
        if(antPathMatcher.match(pattern, request.getRequestURI())){
            chain.doFilter(request,response);
            return;
        }

        System.out.println("인증필터 시작");
        //request 에서 쿠키 얻기.
        Cookie[] cookies = request.getCookies();

        if (cookies != null){
            Optional<Cookie> result = Arrays.stream(cookies).filter(cookie ->
                    cookie.getName().matches("accessToken")).findAny();

            //accessToken 쿠키가 존재할 경우
            if(result.isPresent()){
                Cookie cookie = result.get();
                String accessToken = cookie.getValue();
                System.out.println("---------------");
                System.out.println(accessToken);

                Long memberId = jwtUtil.validateAndExtract(accessToken);

                //accessToken 이 유효할 경우
                if (memberId != null){
                    System.out.println(memberId);
                    Member member = memberRepository.findById(memberId).orElseThrow(() ->
                            new IllegalArgumentException("존재하지 않은 회원입니다."));

                    PrincipalDetails principalDetails = new PrincipalDetails(member);

                    //Authentication 객체 만들기
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    //시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request,response);
    }

}
