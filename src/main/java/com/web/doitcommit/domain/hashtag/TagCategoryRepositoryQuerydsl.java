package com.web.doitcommit.domain.hashtag;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagCategoryRepositoryQuerydsl {

    /**
     * 7일간의 인기태그 상위 8개 리스트
     */
    List<Object[]> getLimitPopularTag();

    /**
     * 7일간의 인기태그 리스트
     */
    List<Object[]> getAllPopularTag();

}
