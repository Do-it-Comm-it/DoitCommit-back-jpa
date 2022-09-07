package com.web.doitcommit.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BoardRepositoryQuerydsl {

    /**
     * 게시글 리스트 조회
     */
    Page<Object[]> getBoardListBySearch(String keyword, Long tagCategoryId, Long boardCategoryId, Pageable pageable);

    /**
     * 게시글 사용자 개수 지정 조회
     */
    List<Object[]> getCustomLimitBoardList(int limit, String order);

    /**
     * 회원별 - 작성한 게시글 리스트 사용자 개수 지정 조회
     */
    List<Board> getCustomLimitBoardListOfMember(int limit, Long memberId);

    /**
     * 북마크 게시글 리스트 조회
     */
    Page<Object[]> getBoardListByBookmark(String keyword, Long tagCategoryId, Long principalId, Pageable pageable);

    /**
     * 게시글의 태그 목록 조회
     */
    List getCustomTagList(Long boardId);
}
