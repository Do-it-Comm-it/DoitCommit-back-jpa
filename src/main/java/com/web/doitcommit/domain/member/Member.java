package com.web.doitcommit.domain.member;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.domain.interestTech.MemberInterestTech;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(length = 100)
    private String selfIntro;

    private String email;

    @OneToOne(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private MemberImage memberImage;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberInterestTech> memberInterestTech = new ArrayList<>();

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

    public void setEmail(String email){
        this.email = email;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeSelfIntro(String selfIntro){
        this.selfIntro = selfIntro;
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

    //연관관계 메서드
    public void setMemberInterestTech(MemberInterestTech memberInterestTech){
        this.memberInterestTech.add(memberInterestTech);
    }
}
