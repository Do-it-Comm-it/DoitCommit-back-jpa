package com.web.doitcommit.domain.files;

import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = {"member"})
@DiscriminatorValue("member")
@Entity
public class MemberImage extends Image {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected MemberImage(){}

    public MemberImage(Member member, String filePath, String fileNm){
        super(member.getMemberId(), filePath, fileNm);
        this.member = member;
        member.setMemberImage(this);
    }
}
