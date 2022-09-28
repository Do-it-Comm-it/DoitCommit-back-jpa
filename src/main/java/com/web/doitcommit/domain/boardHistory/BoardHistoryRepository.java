package com.web.doitcommit.domain.boardHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHistoryRepository extends JpaRepository<BoardHistory,Long>, BoardHistoryRepositoryQuerydsl {

}
