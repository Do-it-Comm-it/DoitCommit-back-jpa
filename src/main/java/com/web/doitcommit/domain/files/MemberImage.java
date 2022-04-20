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

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private boolean socialImg;

    protected MemberImage(){}

    public MemberImage(Member member ,String imageUrl, boolean socialImg, String filePath, String fileNm){
        super(filePath, fileNm);
        this.member = member;
        this.imageUrl = imageUrl;
        this.socialImg = socialImg;
    }
}
