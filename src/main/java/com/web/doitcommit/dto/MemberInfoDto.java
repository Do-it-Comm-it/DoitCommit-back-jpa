package com.web.doitcommit.dto;

import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {

    private String nickname;

    private String email;

    private Set<InterestTech> interestTechSet = new HashSet<InterestTech>();

    private String position;

    private String githubUrl;

    private String url1;

    private String url2;

    private String pictureUrl;

    public MemberInfoDto(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.interestTechSet = member.getInterestTechSet();
        this.position = member.getPosition();
        this.githubUrl = member.getGithubUrl();
        this.url1 = member.getUrl1();
        this.url2 = member.getUrl2();
        this.pictureUrl = member.getPictureUrl();
    }
}
