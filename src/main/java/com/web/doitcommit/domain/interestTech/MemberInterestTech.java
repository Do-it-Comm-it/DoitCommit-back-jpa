package com.web.doitcommit.domain.interestTech;

import com.web.doitcommit.domain.member.Member;
import lombok.Getter;
import lombok.ToString;
import javax.persistence.*;

@ToString(exclude = {"member"})
@Getter
@Entity
public class MemberInterestTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberInterestTech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "interest_tech_id")
    private InterestTech interestTech;

    protected MemberInterestTech(){}

    public MemberInterestTech(Member member, InterestTech interestTech) {
        this.member = member;
        this.interestTech = interestTech;
        member.setMemberInterestTech(this);
    }
}
