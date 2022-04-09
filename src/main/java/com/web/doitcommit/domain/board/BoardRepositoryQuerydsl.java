package com.web.doitcommit.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BoardRepositoryQuerydsl {

    /**
     * 게시판 목록 조회
     */
    Page<Object[]> getBoardListBySearch(String keyword, Long tagCategoryId, Long boardCategoryId, Pageable pageable);

    /**
     * 게시글 사용자 개수 지정 조회
     */
    List<Object[]> getCustomLimitBoardList(int limit, Long boardCategoryId);

    /**
     * 게시글의 태그 목록 조회
     */
    List getCustomTagList(Long boardId);
}
