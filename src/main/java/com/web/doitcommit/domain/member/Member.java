package com.web.doitcommit.domain.member;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.files.MemberImage;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(unique = true)
    public String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false, unique = true)
    private String oauthId;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private String role = "USER";

    private String username;

    private String email;

    @Builder.Default
    private String pictureUrl = "";

    @OneToOne(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private MemberImage memberImage;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    @Builder.Default
    private Set<String> interestTechSet = new HashSet<>();

    private String position;

    private String githubUrl;

    private String url1;

    private String url2;

    //회원 상태 : activate : 일반, deactivate : 탈퇴 중
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StateType state = StateType.activate;

    private LocalDateTime leaveDate;

    @PrePersist
    public void defaultState() {
        this.state = this.state == null ? StateType.activate : this.state;
    }

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

    public void changeInterestTechSet(Set<String> interestTechSet){
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

    public void changeState(StateType state){
        this.state = state;
    }

    public void changePosition(String position){
        this.position = position;
    }

    public void changeLeaveDate(LocalDateTime leaveDate){
        this.leaveDate = leaveDate;
    }


}
