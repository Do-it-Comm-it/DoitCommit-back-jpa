package com.web.doitcommit.cofig.oAuth;

import com.web.doitcommit.cofig.auth.PrincipalDetails;
import com.web.doitcommit.cofig.oAuth.userInfo.OAuth2UserInfo;
import com.web.doitcommit.cofig.oAuth.userInfo.OAuth2UserInfoFactory;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.web.doitcommit.domain.member.AuthProvider.GITHUB;
import static com.web.doitcommit.domain.member.AuthProvider.GOOGLE;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // oauth 제공해주는 사이트마다 제공해주는 데이터 변수명이 다름
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //로그인한 사이트
        AuthProvider authProvider =
                AuthProvider.valueOf(
                        userRequest.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, attributes);

        //신규,기존 회원 체크
        Optional<Member> result = memberRepository.findByOauthId(userInfo.getId());

        //신규 회원
        if(result.isEmpty()){
            Member member;

            //랜덤값 비밀번호 생성
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());

            switch (authProvider) {
                case GOOGLE: member = Member.builder()
                        .oauthId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
                        .email(userInfo.getEmail())
                        .provider(GOOGLE)
                        .build();

                    if (userInfo.getImageUrl() != null){
                        member.setPicture(userInfo.getImageUrl());
                    }
                    memberRepository.save(member);
                    break;

                case GITHUB: member = Member.builder()
                        .oauthId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
                        .provider(GITHUB)
                        .githubUrl((String)attributes.get("html_url"))
                        .pictureUrl(userInfo.getImageUrl())
                        .build();
                
                    if (userInfo.getEmail() != null){
                        member.setEmail(userInfo.getEmail());
                    }
                    memberRepository.save(member);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + authProvider);
            }

            return new PrincipalDetails(member, userInfo);

        }
        // 기존 회원
        else{
            return new PrincipalDetails(result.get(), userInfo);
        }

    }
}
