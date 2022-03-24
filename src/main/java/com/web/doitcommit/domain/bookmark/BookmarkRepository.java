package com.web.doitcommit.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.member.memberId = :memberId and b.board.boardId = :boardId")
    void deleteBookmark(@Param("memberId") Long memberId, @Param("boardId") Long boardId);

}
