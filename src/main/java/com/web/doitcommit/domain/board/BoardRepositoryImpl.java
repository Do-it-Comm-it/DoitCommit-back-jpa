package com.web.doitcommit.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.doitcommit.domain.hashtag.QTagCategory;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import static com.web.doitcommit.domain.board.QBoard.board;

public class BoardRepositoryImpl implements BoardRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 게시판 목록 조회
     */
    @Override
    public List<Board> getCustomBoardList(int pageNo, int pageSize) {
        List<Long> ids = queryFactory
                .select(board.boardId)
                .from(board)
                .orderBy(board.boardId.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(board)
                .from(board)
                .where(board.boardId.in(ids))
                .orderBy(board.boardId.desc())
                .fetch();
    }

    /**
     * 태그 목록 조회
     */
    @Override
    public List<String> getCustomTagList() {
        QTagCategory tagCategory = QTagCategory.tagCategory;

        return queryFactory
                .select(tagCategory.tagName)
                .from(tagCategory)
                .orderBy(tagCategory.tagName.asc())
                .fetch();
    }
}
