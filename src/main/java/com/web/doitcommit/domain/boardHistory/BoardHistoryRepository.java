package com.web.doitcommit.domain.boardHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardHistoryRepository extends JpaRepository<BoardHistory,Long>, BoardHistoryRepositoryQuerydsl {

    @Modifying(clearAutomatically = true)
    @Query("delete from BoardHistory bhi where bhi.board.boardId = :boardId and bhi.member.memberId =:memberId")
    void deleteBoardHistory(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from BoardHistory bhi where bhi.board.boardId in (:boardIds) and bhi.member.memberId =:memberId")
    void deleteMultipleBoardHistory(@Param("boardIds") List<Long> boardIdList, @Param("memberId") Long memberId);
}
