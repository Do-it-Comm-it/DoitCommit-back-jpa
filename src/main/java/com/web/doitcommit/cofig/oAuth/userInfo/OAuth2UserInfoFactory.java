package com.web.doitcommit.cofig.oAuth.userInfo;

import com.web.doitcommit.domain.member.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider,
                                                   Map<String, Object> attributes) {
        switch (provider) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case GITHUB: return new GitHubOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
