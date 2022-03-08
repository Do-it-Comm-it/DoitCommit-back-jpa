package com.web.doitcommit.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryQuerydsl {

    /**
     * 7일간의 인기태그 상위 8개 리스트
     */
    @Query(value = "select bt.tag, count(bt.tag) as cnt from board b join board_tag bt on bt.board_id = b.board_id " +
            "where b.mod_date BETWEEN DATE_ADD(NOW(),INTERVAL -7 DAY ) AND NOW()" +
            "group by bt.tag " +
            "order by cnt desc " +
            "limit 8", nativeQuery = true)
    List<Object[]> getLimitPopularTag();

    /**
     * 7일간의 인기태그 리스트
     */
    @Query(value = "select bt.tag, count(bt.tag) as cnt from board b join board_tag bt on bt.board_id = b.board_id " +
            "where b.mod_date BETWEEN DATE_ADD(NOW(),INTERVAL -7 DAY ) AND NOW()" +
            "group by bt.tag ", nativeQuery = true)
    List<Object[]> getAllPopularTag();
}
