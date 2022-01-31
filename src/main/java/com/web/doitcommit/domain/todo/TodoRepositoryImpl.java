package com.web.doitcommit.domain.todo;

import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.el.lang.ExpressionBuilder;

import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class TodoRepositoryImpl implements TodoRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 사용자 개수 지정 투두 리스트 조회
     *
     * 1. 당일날짜 투두, 회원별
     * 2. 1) 상단고정순 2) 완료상태순 3) 작성순
     */
    @Override
    public List<Todo> getCustomLimitTodoList(int limit, Long memberId) {
        QTodo todo = QTodo.todo;

        return queryFactory.selectFrom(todo)
                .where(todo.member.memberId.eq(memberId),
                        todo.todoDateTime.year().eq(LocalDate.now().getYear()),
                        todo.todoDateTime.month().eq(LocalDate.now().getMonthValue()),
                        todo.todoDateTime.dayOfMonth().eq(LocalDate.now().getDayOfMonth()))
                .orderBy(todo.isFixed.desc(), todo.isFinished.asc(), todo.todoId.asc())
                .limit(limit)
                .fetch();
    }
}
