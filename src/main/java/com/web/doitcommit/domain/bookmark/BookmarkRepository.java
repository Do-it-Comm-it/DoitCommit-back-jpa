package com.web.doitcommit.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark bm where bm.member.memberId = :memberId and bm.board.boardId = :boardId")
    void deleteBookmark(@Param("memberId") Long memberId, @Param("boardId") Long boardId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark bm where bm.member.memberId =:memberId and bm.board.boardId in (:boardIds)")
    void deleteMultipleBookmark(@Param("memberId") Long memberId, @Param("boardIds") List<Long> boardIdList);

}
