package com.web.doitcommit.domain.member;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.interestTech.InterestTech;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Member extends BaseEntity {

    private String username;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    public String nickname;

    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false, unique = true)
    private String oauthId;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<InterestTech> interestTechSet = new HashSet<InterestTech>();

    private String position;

    @Builder.Default
    @Column(nullable = false)
    private String role = "USER";

    private String githubUrl;

    private String url1;

    private String url2;

    @Builder.Default
    @Column
    private String pictureUrl = "";

    public void setPicture(String pictureUrl){
        this.pictureUrl = pictureUrl;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeInterestTechSet(Set<InterestTech> interestTechSet){
        this.interestTechSet = interestTechSet;
    }

    public void changeGithubUrl(String githubUrl){
        this.githubUrl = githubUrl;
    }

    public void changeUrl1(String url1){
        this.url1 = url1;
    }

    public void changeUrl2(String url2){
        this.url2 = url2;
    }

    public void changePictureUrl(String pictureUrl){
        this.pictureUrl = pictureUrl;
    }




}
