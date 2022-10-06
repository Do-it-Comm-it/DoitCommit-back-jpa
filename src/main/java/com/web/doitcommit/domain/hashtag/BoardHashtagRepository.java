package com.web.doitcommit.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long>, BoardHashtagRepositoryQuerydsl {
}
