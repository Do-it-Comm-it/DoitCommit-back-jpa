package com.web.doitcommit.domain.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.doitcommit.domain.boardHashtag.QBoardHashtag;
import com.web.doitcommit.domain.boardHistory.QBoardHistory;
import com.web.doitcommit.domain.bookmark.QBookmark;

import com.web.doitcommit.domain.hashtagCategory.QHashtagCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.web.doitcommit.domain.board.QBoard.board;
import static com.web.doitcommit.domain.boardHistory.QBoardHistory.boardHistory;
import static com.web.doitcommit.domain.bookmark.QBookmark.bookmark;
import static com.web.doitcommit.domain.files.QBoardImage.boardImage;
import static com.web.doitcommit.domain.files.QImage.image;
import static com.web.doitcommit.domain.files.QMemberImage.memberImage;
import static org.apache.commons.lang3.ObjectUtils.min;
import static org.aspectj.util.LangUtil.isEmpty;

public class BoardRepositoryImpl implements BoardRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 게시판 리스트 조회
     */
    @Override
    public Page<Object[]> getBoardListBySearch(String keyword, Long HashtagCategoryId,
                                                        Long boardCategoryId, Pageable pageable) {

        List<Tuple> results = queryFactory
                .select(board, memberImage, board.commentList.size(), board.heartList.size())
                .from(board)
                .join(board.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(board.member))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId))
                .orderBy(sort(pageable).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Tuple> countQuery = queryFactory
                .select(board, memberImage, board.commentList.size(), board.heartList.size())
                .from(board)
                .join(board.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(board.member))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        /**
         * 두 경우에는 count 쿼리를 날리지 않음 -> spring Jpa가 지원 (PageableExeutionUtils)
         * 1. 시작 페이지이면서 content 수가 page size보다 작을 경우.
         * 2. 마지막 페이지이면서 content 수가 page size보다 작을 경우.
         * 날릴 필요가 없음.
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }


    /**
     * 게시글 사용자 개수 지정 조회
     */
    @Override
    public List<Object[]> getCustomLimitBoardList(int limit, String order) {
        OrderSpecifier<?> orderBy = board.boardId.desc();
        switch(order) {
            case "boardCnt":
                orderBy = board.boardCnt.desc();
                break;
            default:
                break;
        }

        List<Tuple> results = queryFactory
                .select(board, memberImage, board.commentList.size())
                .from(board)
                .join(board.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(board.member))
                .orderBy(orderBy)
                .limit(limit)
                .fetch();

        return results.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 회원별 - 작성한 게시글 리스트 사용자 개수 지정 조회
     */
    @Override
    public List<Board> getCustomLimitBoardListOfMember(int limit, Long memberId) {

        return queryFactory.select(board)
                .from(board)
                .where(board.member.memberId.eq(memberId))
                .orderBy(board.boardCnt.desc())
                .limit(limit)
                .fetch();
    }


    /**
     * 북마크 게시글 리스트 조회
     */
    @Override
    public Page<Object[]> getBoardListByBookmark(String keyword, Long HashtagCategoryId, Long boardCategoryId, Long principalId, Pageable pageable) {

        List<Tuple> results = queryFactory
                .select(board, memberImage, board.commentList.size(), board.heartList.size())
                .from(board)
                .join(board.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(board.member))
                .leftJoin(bookmark).on(bookmark.board.boardId.eq(board.boardId))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId),
                        bookmark.member.memberId.eq(principalId))
                .orderBy(bookmark.bookmarkId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .leftJoin(bookmark).on(bookmark.board.boardId.eq(board.boardId))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId),
                        bookmark.member.memberId.eq(principalId));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 게시글 조회 히스토리 내역 조회
     */
    @Override
    public Page<Object[]> getBoardListByBoardHistory(String keyword, Long HashtagCategoryId, Long boardCategoryId, Long principalId, Pageable pageable) {

        List<Tuple> results = queryFactory
                .select(board, memberImage, board.commentList.size(), board.heartList.size())
                .from(board)
                .join(board.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(board.member))
                .join(boardHistory).on(boardHistory.board.eq(board))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId),
                        boardHistory.member.memberId.eq(principalId))
                .orderBy(boardHistory.viewDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .join(boardHistory).on(boardHistory.board.eq(board))
                .where(categorySearch(boardCategoryId),
                        keywordSearch(keyword), hashtagSearch(HashtagCategoryId),
                        boardHistory.member.memberId.eq(principalId));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private BooleanExpression hashtagSearch(Long HashtagCategoryId) {
        return HashtagCategoryId != null ? board.boardHashtag.any().hashtagCategory.hashtagId.eq(HashtagCategoryId) : null;
    }

    private BooleanExpression keywordSearch(String keyword){
        return !isEmpty(keyword) ? titleContain(keyword).or(contentContain(keyword)) : null;
    }

    private BooleanExpression contentContain(String keyword) {
        return !isEmpty(keyword) ? board.boardContent.contains(keyword):null;
    }

    private BooleanExpression titleContain(String keyword) {
        return !isEmpty(keyword) ? board.boardTitle.contains(keyword):null;
    }

    private BooleanExpression categorySearch(Long boardCategoryId) {
        return boardCategoryId != null ? board.boardCategory.categoryId.eq(boardCategoryId) : null;
    }

    private List<OrderSpecifier<?>> sort(Pageable page) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()) {
                    case "HEART":
                        orders.add(new OrderSpecifier(direction, board.heartList.size()));
                        break;
                    case "VIEW":
                        orders.add(new OrderSpecifier(direction, board.boardCnt));
                        break;
                    case "DESC":
                        orders.add(new OrderSpecifier(direction, board.boardId));
                    default:
                        break;
                }
            }
        }
        return orders;
    }


    /**
     * 게시글의 태그 목록 조회
     * @return
     */
    @Override
    public List getCustomTagList(Long boardId) {
        QHashtagCategory hashtagCategory = QHashtagCategory.hashtagCategory;
        QBoardHashtag boardHashtag = QBoardHashtag.boardHashtag;

        List<Tuple> results = queryFactory
                .select(hashtagCategory.hashtagId, hashtagCategory.tagName)
                .from(hashtagCategory)
                .leftJoin(boardHashtag).on(hashtagCategory.hashtagId.eq(boardHashtag.hashtagCategory.hashtagId))
                .where(boardHashtag.board.boardId.eq(boardId))
                .fetch();

        
        List tagList = new ArrayList();
        for (Tuple result : results) {
            Map<Long,String> tagMap = new HashMap<>();
            tagMap.put(result.get(hashtagCategory.hashtagId), result.get(hashtagCategory.tagName));
            tagList.add(tagMap);
        }

        return tagList;
    }
}
