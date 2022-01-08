package com.web.doitcommit.filter;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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

@Component
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${app.token.accessTokenName}")
    private String accessTokenName;

    @Value("${app.token.refreshTokenName}")
    private String refreshTokenName;

    private AntPathMatcher antPathMatcher;
    private String pattern = "/auth/**/*";
    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private CookieUtil cookieUtil;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil, MemberRepository memberRepository, CookieUtil cookieUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        Long memberId = null;
        String accessJwt = null;
        String refreshJwt = null;
        String refreshUname = null;

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
                    cookie.getName().matches(accessTokenName)).findAny();

            //accessToken 쿠키가 존재할 경우
            if(result.isPresent()){
                Cookie accessToken = cookieUtil.getCookie(request, accessTokenName);
                accessJwt = accessToken.getValue();

                try {
                    memberId = jwtUtil.getMemberIdFromToken(accessJwt);
                //잘못된 토큰일때
                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                //만료시간이 지났을때
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                    request.setAttribute("token-expired", true);
                    //auth/refreshToken api를 요청해서 리프레쉬 토큰확인해서 새로운 엑세스토큰과 리프레쉬토큰을 만들어서 쿠키에 넣어줌
                    //여기서 필터 다시시작 어떻게할지?
                }

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
