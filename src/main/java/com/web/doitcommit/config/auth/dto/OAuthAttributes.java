package com.web.doitcommit.config.auth.dto;

import com.web.doitcommit.domain.memberRole.Role;
import com.web.doitcommit.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String nameAttributeName, Map<String, Object> attributes){

        return ofGoogle(nameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String nameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(nameAttributeName)
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .oauthId(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST) // 기본 권한 GUEST
                .build();
    }
}