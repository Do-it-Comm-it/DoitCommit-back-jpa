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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String username;

    @Column(unique = true, length = 12)
    private String nickname;

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
    @Column(nullable = false)
    private String pictureUrl = "";

    public void setPicture(String pictureUrl){
        this.pictureUrl = pictureUrl;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
