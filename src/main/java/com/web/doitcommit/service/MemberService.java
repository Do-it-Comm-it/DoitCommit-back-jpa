package com.web.doitcommit.service;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.MemberDto;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    public MemberDto reqGetMemberInfo(HttpServletRequest request) {

        Cookie cookie = cookieUtil.getCookie(request, jwtUtil.accessTokenName);
        String accessToken = cookie.getValue();
        Long memberId = jwtUtil.validateAndExtract(accessToken);

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));

        return new MemberDto(member);
    }
}
