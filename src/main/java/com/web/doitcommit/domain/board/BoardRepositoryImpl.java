package com.web.doitcommit.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Board> getCustomBoardList(int pageNo, int pageSize) {
        return null;
    }
}
