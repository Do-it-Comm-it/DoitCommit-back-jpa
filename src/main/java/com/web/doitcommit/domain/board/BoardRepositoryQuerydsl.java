package com.web.doitcommit.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryQuerydsl {

    /**
     * 게시판 목록 조회
     */
    Page<Object[]> getSearchByKeywordOfBoardList(String keyword, Long boardCategoryId, Pageable pageable);

    /**
     * 게시글의 태그 목록 조회
     */
    List getCustomTagList(Long boardId);
}
