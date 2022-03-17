package com.web.doitcommit.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select c, i from Comment c " +
            "left join MemberImage mi on mi.member.memberId = c.member.memberId " +
            "left join Image i on i.imageId = mi.imageId " +
            "where c.board.boardId = :boardId")
    Page<Object[]> getCommentList(@Param("boardId") Long boardId, Pageable pageable);

    @Query("select m.memberId, m.nickname, i from Comment c " +
            "join Member m on m.memberId = c.member.memberId " +
            "left join MemberImage mi on mi.member.memberId = m.memberId " +
            "left join Image i on i.imageId = mi.imageId " +
            "where c.board.boardId = :boardId " +
            "group by m.memberId")
    List<Object[]> getMemberTagList(@Param("boardId") Long boardId);
}
