package com.web.doitcommit.cofig.oauth;

import com.web.doitcommit.cofig.auth.PrincipalDetails;
import com.web.doitcommit.cofig.oauth.userInfo.OAuth2UserInfo;
import com.web.doitcommit.cofig.oauth.userInfo.OAuth2UserInfoFactory;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // oauth 사이트마다 제공해주는 데이터 이름이 다름
        Map<String, Object> attributes = oAuth2User.getAttributes();

        AuthProvider authProvider =
                AuthProvider.valueOf(
                        userRequest.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, attributes);

        Optional<Member> result = memberRepository.findByOauthId(userInfo.getId());

        if(result.isEmpty()){
            Member member = Member.builder()
                    .oauthId(userInfo.getId())
                    .password("1111")
                    .username(userInfo.getName())
                    .email(userInfo.getEmail())
                    .nickname("test")
                    .provider(AuthProvider.GOOGLE)
                    .build();

            memberRepository.save(member);

            return new PrincipalDetails(member, userInfo);

        }else{
            return new PrincipalDetails(result.get(), userInfo);
        }

    }
}
