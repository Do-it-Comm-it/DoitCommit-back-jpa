package com.web.doitcommit.dto;

import com.web.doitcommit.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

//    private Long memberId;

    private String username;

    private String nickname;

    private String email;

//    private AuthProvider provider;
//
//    private String oauthId;
//
//    private String password;

//    private Set<InterestTech> interestTechSet = new HashSet<InterestTech>();

    private String position;

//    private String role;

    private String githubUrl;

    private String url1;

    private String url2;

    private String pictureUrl;

    public MemberDto(Member entity){
        this.username = entity.username;
        this.nickname = entity.nickname;
        this.email = entity.email;
        this.position = entity.position;
        this.githubUrl = entity.githubUrl;
        this.url1 = entity.url1;
        this.url2 = entity.url2;
        this.pictureUrl = entity.pictureUrl;
    }

}
