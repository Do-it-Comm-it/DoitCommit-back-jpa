package com.web.doitcommit.domain.boardHistory;

import java.util.Optional;

public interface BoardHistoryRepositoryQuerydsl {

    Optional<BoardHistory> findByBoardIdAndMemberId(Long boardId, Long memberId);


}
