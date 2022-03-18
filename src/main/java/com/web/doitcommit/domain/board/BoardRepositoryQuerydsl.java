package com.web.doitcommit.domain.board;

import java.util.List;

public interface BoardRepositoryQuerydsl {

    /**
     * 게시판 목록 조회
     */
    List<Board> getCustomBoardList(int pageNo, int pageSize);

    /**
     * 게시글의 태그 목록 조회
     */
    /*List<Tuple> getCustomTagList();*/
}
