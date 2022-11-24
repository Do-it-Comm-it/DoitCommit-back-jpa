package com.web.doitcommit.service.boardHistory;

import java.util.List;

public interface BoardHistoryService {

    void remove(Long boardId, Long memberId);

    void removeMultiple(Long principalId, List<Long> boardIdList);

}
