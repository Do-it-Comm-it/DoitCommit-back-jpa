package com.web.doitcommit.domain.hashtag;

import com.web.doitcommit.domain.files.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long> {
}
