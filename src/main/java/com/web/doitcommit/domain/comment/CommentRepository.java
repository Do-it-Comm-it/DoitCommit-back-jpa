package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select m.memberId, m.nickname, i from Comment c " +
            "join Member m on m.memberId = c.member.memberId " +
            "left join MemberImage mi on mi.member.memberId = m.memberId " +
            "left join Image i on i.imageId = mi.imageId " +
            "where c.board.boardId = :boardId")
    List<Object[]> getTagMemberList(@Param("boardId") Long boardId);
}
