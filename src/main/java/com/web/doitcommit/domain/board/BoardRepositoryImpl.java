package com.web.doitcommit.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
     * 게시글의 태그 목록 조회
     */
/*    @Override
    public List<Tuple> getCustomTagList() {
        QTagCategory tagCategory = QTagCategory.tagCategory;
        QBoardHashtag boardHashtag = QBoardHashtag.boardHashtag;

        return queryFactory
                .select(tagCategory.tagId, tagCategory.tagName)
                .from(tagCategory)
                .join(boardHashtag.tagCategory, tagCategory)
                .where(boardHashtag.board.boardId.eq(2L))
                .fetch();
    }*/
}
