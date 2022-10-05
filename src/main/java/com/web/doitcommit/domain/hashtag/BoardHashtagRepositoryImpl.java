package com.web.doitcommit.domain.hashtag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import static com.web.doitcommit.domain.hashtag.QBoardHashtag.boardHashtag;

public class BoardHashtagRepositoryImpl implements BoardHashtagRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public BoardHashtagRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * boardId로 boardHashtag 삭제
     */
    @Override
    public Long deleteBoardHashtagByBoardId(Long boardId) {

        Long result = queryFactory
                .delete(boardHashtag)
                .where(boardHashtag.board.boardId.eq(boardId))
                .execute();
        return result;
    }
}
