package com.web.doitcommit.domain.hashtag;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.doitcommit.domain.board.QBoard;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.web.doitcommit.domain.board.QBoard.board;
import static com.web.doitcommit.domain.hashtag.QBoardHashtag.boardHashtag;
import static com.web.doitcommit.domain.hashtag.QTagCategory.tagCategory;
import static io.lettuce.core.GeoArgs.Sort.desc;

public class TagCategoryRepositoryImpl implements TagCategoryRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public TagCategoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 7일간의 인기태그 상위 8개 리스트
     */
    @Override
    public List<Object[]> getLimitPopularTag() {

        List<Tuple> result = queryFactory.select(tagCategory.tagId, tagCategory.tagName, tagCategory.tagId.count())
                .from(tagCategory)
                .join(boardHashtag).on(boardHashtag.tagCategory.tagId.eq(tagCategory.tagId))
                .join(board).on(board.boardId.eq(boardHashtag.board.boardId))
                .where(board.modDate.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .groupBy(tagCategory.tagId)
                .orderBy(tagCategory.count().desc())
                .limit(8)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 7일간의 인기태그 리스트
     */
    @Override
    public List<Object[]> getAllPopularTag() {

        List<Tuple> result = queryFactory.select(tagCategory.tagId, tagCategory.tagName, tagCategory.tagId.count())
                .from(tagCategory)
                .join(boardHashtag).on(boardHashtag.tagCategory.tagId.eq(tagCategory.tagId))
                .join(board).on(board.boardId.eq(boardHashtag.board.boardId))
                .where(board.modDate.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .groupBy(tagCategory.tagId)
                .orderBy(tagCategory.count().desc())
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());

    }
}
