package com.web.doitcommit.domain.member;

import java.util.List;

public interface MemberRepositoryQuerydsl {

    /**
     * 멤버 이미지 url 조회
     */
    String getMemberImage(Long memberId);

    /**
     * 관심기술 목록 조회
     */
    List getCustomInterestTechList(Long boardId);

}
