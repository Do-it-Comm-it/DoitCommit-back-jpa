package com.web.doitcommit.cofig.oAuth;

import com.web.doitcommit.cofig.auth.PrincipalDetails;
import com.web.doitcommit.cofig.oAuth.userInfo.OAuth2UserInfo;
import com.web.doitcommit.cofig.oAuth.userInfo.OAuth2UserInfoFactory;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // oauth 사이트마다 제공해주는 데이터 이름이 다름
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println(attributes);

        //깃헙 : {login=hyeongwoo-LEE, id=77663506, node_id=MDQ6VXNlcjc3NjYzNTA2,
        // avatar_url=https://avatars.githubusercontent.com/u/77663506?v=4, gravatar_id=,
        // url=https://api.github.com/users/hyeongwoo-LEE,
        // html_url=https://github.com/hyeongwoo-LEE,
        // followers_url=https://api.github.com/users/hyeongwoo-LEE/followers,
        // following_url=https://api.github.com/users/hyeongwoo-LEE/following{/other_user},
        // gists_url=https://api.github.com/users/hyeongwoo-LEE/gists{/gist_id},
        // starred_url=https://api.github.com/users/hyeongwoo-LEE/starred{/owner}{/repo},
        // subscriptions_url=https://api.github.com/users/hyeongwoo-LEE/subscriptions,
        // organizations_url=https://api.github.com/users/hyeongwoo-LEE/orgs,
        // repos_url=https://api.github.com/users/hyeongwoo-LEE/repos,
        // events_url=https://api.github.com/users/hyeongwoo-LEE/events{/privacy},
        // received_events_url=https://api.github.com/users/hyeongwoo-LEE/received_events,
        // type=User, site_admin=false, name=null, company=null, blog=, location=null,
        // email=null, hireable=null, bio=null, twitter_username=null, public_repos=18,
        // public_gists=0, followers=7, following=7, created_at=2021-01-19T08:00:37Z,
        // updated_at=2021-12-27T11:27:23Z}

        AuthProvider authProvider =
                AuthProvider.valueOf(
                        userRequest.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, attributes);

        Optional<Member> result = memberRepository.findByOauthId(userInfo.getId());

        if(result.isEmpty()){

            Member member;
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

        }else{
            return new PrincipalDetails(result.get(), userInfo);
        }

    }
}
