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
import static com.web.doitcommit.domain.hashtag.QHashtagCategory.hashtagCategory;
import static io.lettuce.core.GeoArgs.Sort.desc;

public class HashtagCategoryRepositoryImpl implements HashtagCategoryRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public HashtagCategoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 지정된 기간동안 인기태그 상위 8개 리스트
     */
    @Override
    public List<Object[]> getLimitPopularTagListForPeriod(int period) {

        List<Tuple> result = queryFactory.select(hashtagCategory.hashtagId, hashtagCategory.tagName, hashtagCategory.hashtagId.count())
                .from(hashtagCategory)
                .join(boardHashtag).on(boardHashtag.hashtagCategory.hashtagId.eq(hashtagCategory.hashtagId))
                .join(board).on(board.boardId.eq(boardHashtag.board.boardId))
                .where(board.modDate.between(LocalDateTime.now().minusDays(period), LocalDateTime.now()))
                .groupBy(hashtagCategory.hashtagId)
                .orderBy(hashtagCategory.count().desc())
                .limit(8)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 전체 태그 상위 8개 리스트
     */
    @Override
    public List<Object[]> getLimitPopularTagList() {

        List<Tuple> result = queryFactory.select(hashtagCategory.hashtagId, hashtagCategory.tagName, hashtagCategory.hashtagId.count())
                .from(hashtagCategory)
                .join(boardHashtag).on(boardHashtag.hashtagCategory.hashtagId.eq(hashtagCategory.hashtagId))
                .join(board).on(board.boardId.eq(boardHashtag.board.boardId))
                .groupBy(hashtagCategory.hashtagId)
                .orderBy(hashtagCategory.count().desc())
                .limit(8)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 지정된 기간동안 인기태그 리스트
     */
    @Override
    public List<Object[]> getAllPopularTagListForPeriod(int period) {

        List<Tuple> result = queryFactory.select(hashtagCategory.hashtagId, hashtagCategory.tagName, hashtagCategory.hashtagId.count())
                .from(hashtagCategory)
                .join(boardHashtag).on(boardHashtag.hashtagCategory.hashtagId.eq(hashtagCategory.hashtagId))
                .join(board).on(board.boardId.eq(boardHashtag.board.boardId))
                .where(board.modDate.between(LocalDateTime.now().minusDays(period), LocalDateTime.now()))
                .groupBy(hashtagCategory.hashtagId)
                .orderBy(hashtagCategory.count().desc())
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());

    }
}
