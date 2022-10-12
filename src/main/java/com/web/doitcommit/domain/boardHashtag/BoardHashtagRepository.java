package com.web.doitcommit.domain.boardHashtag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long>, BoardHashtagRepositoryQuerydsl {
}
