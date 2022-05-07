package com.web.doitcommit.domain.member;

public interface MemberRepositoryQuerydsl {

    /**
     * 멤버 이미지 url 조회
     */
    String getMemberImage(Long memberId);

}
