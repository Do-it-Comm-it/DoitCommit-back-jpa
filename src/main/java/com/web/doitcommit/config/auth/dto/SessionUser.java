package com.web.doitcommit.config.auth.dto;

import com.web.doitcommit.domain.member.Member;
import lombok.Getter;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long memberId;
    private String nickname;
    private String oauthId;

    public SessionUser(Member member){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.oauthId = member.getOauthId();
    }
}