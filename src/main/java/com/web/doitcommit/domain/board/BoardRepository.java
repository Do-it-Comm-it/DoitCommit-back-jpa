package com.web.doitcommit.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryQuerydsl {

    /**
     * 댓글 회원 태그 - 게시글 작성자 태그
     */
    @Query("select m.memberId, m.nickname,i from Board b " +
            "left join Member m on m.memberId = b.member.memberId " +
            "left join MemberImage mi on mi.member.memberId = m.memberId " +
            "left join Image i on i.imageId = mi.imageId " +
            "where b.boardId = :boardId")
    List<Object[]> getMemberTagOfWriter(@Param("boardId") Long boardId);

    /**
     * 회원이 작성한 게시글 총 개수 조회
     */
    @Query("select count(b) from Board b where b.member.memberId =:memberId")
    long countByMemberId(@Param("memberId") Long memberId);
}
