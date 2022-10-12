package com.web.doitcommit.domain.boardHashtag;

public interface BoardHashtagRepositoryQuerydsl {

    /**
     * boardId로 boardHashtag 삭제
     */
    Long deleteBoardHashtagByBoardId(Long boardId);

}
