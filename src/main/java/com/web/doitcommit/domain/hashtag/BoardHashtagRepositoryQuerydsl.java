package com.web.doitcommit.domain.hashtag;

public interface BoardHashtagRepositoryQuerydsl {

    /**
     * boardId로 boardHashtag 삭제
     */
    Long deleteBoardHashtagByBoardId(Long boardId);

}
