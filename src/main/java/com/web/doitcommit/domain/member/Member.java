package com.web.doitcommit.domain.member;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.memberRole.MemberRole;
import com.web.doitcommit.domain.memberRole.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true, length = 6)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String oauthId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private Role role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "interest_tech", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<InterestTech> interestTechSet = new HashSet<InterestTech>();

    private String position;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<MemberRole>(Arrays.asList(MemberRole.USER));

    private String githubUrl;

    private String url1;

    private String url2;

    @Builder.Default
    @Column(nullable = false)
    private String folderPath = "";

    @Builder.Default
    @Column(nullable = false)
    private String filename = "";

    @Builder
    public Member(String oauthId, String email, String picture, Role role){
        this.oauthId = oauthId;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public Member update(String oauthId, String picture){
        this.oauthId = oauthId;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
