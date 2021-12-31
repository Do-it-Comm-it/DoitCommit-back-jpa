package com.web.doitcommit.config.auth;

import com.web.doitcommit.config.auth.dto.OAuthAttributes;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.memberRole.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> attributesMap = oAuth2User.getAttributes();
        String sub = (String) attributesMap.get("sub");

        Optional<Member> result = memberRepository.findByOauthId(sub);

        if(result.isEmpty()){ //새로운회원
            Member member = Member.builder()
                    .username(attributes.getName())
                    .nickname("test")
                    .password("test")
                    .oauthId(sub)
                    .email(attributes.getEmail())
                    .picture(attributes.getPicture())
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);

        }else{//기존회원

        }
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
}