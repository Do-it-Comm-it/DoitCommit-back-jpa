package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.TagCategory;

import java.util.List;

public interface BoardRepositoryQuerydsl {

    /**
     * 게시판 목록 조회
     */
    List<Board> getCustomBoardList(int pageNo, int pageSize);

    /**
     * 태그 목록 조회
     */
    List<String> getCustomTagList();
}
