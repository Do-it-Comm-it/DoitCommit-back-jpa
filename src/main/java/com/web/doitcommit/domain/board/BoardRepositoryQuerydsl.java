package com.web.doitcommit.domain.board;

import java.util.List;

public interface BoardRepositoryQuerydsl {
    List<Board> getCustomBoardList(int pageNo, int pageSize);
}
