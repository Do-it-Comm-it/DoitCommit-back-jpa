package com.web.doitcommit.domain.member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryQuerydsl {

    /**
     * 멤버 이미지 url 조회
     */
    String getMemberImage(Long memberId);

    /**
     * 관심기술 목록 조회
     */
    List getCustomInterestTechList(Long boardId);

    /**
     * 회원 조회 - 프로필 이미지 포함
     */
    Optional<Object[]> findWithImageByMemberId(Long memberId);

}
