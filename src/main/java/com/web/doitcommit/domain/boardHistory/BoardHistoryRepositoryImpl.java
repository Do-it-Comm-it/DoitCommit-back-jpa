package com.web.doitcommit.domain.boardHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.web.doitcommit.domain.boardHistory.QBoardHistory.boardHistory;

public class BoardHistoryRepositoryImpl implements BoardHistoryRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public BoardHistoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * boarId, memberId로 히스토리 내역 조회
     */
    @Override
    public Optional<BoardHistory> findByBoardIdAndMemberId(Long boardId, Long memberId) {

        BoardHistory findBoardHistory =
                queryFactory.selectFrom(boardHistory)
                        .where(boardHistory.board.boardId.eq(boardId),
                                boardHistory.member.memberId.eq(memberId))
                .fetchOne();

        return findBoardHistory != null ? Optional.of(findBoardHistory) : Optional.empty();
    }
}
