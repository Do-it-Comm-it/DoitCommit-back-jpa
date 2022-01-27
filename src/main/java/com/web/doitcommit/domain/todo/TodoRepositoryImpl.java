package com.web.doitcommit.domain.todo;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class TodoRepositoryImpl implements TodoRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 사용자 개수 지정 투두 리스트 조회
     */
    @Override
    public List<Todo> getCustomLimitTodoList(int limit, Long memberId) {
        QTodo todo = QTodo.todo;

        return queryFactory.selectFrom(todo)
                .where(todo.member.memberId.eq(memberId))
                .orderBy(todo.isFixed.desc())
                .limit(limit)
                .fetch();
    }
}
