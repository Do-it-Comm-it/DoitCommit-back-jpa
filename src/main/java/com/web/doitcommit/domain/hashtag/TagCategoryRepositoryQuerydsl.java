package com.web.doitcommit.domain.hashtag;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagCategoryRepositoryQuerydsl {

    /**
     * 지정된 기간동안 인기태그 상위 8개 리스트
     */
    List<Object[]> getLimitPopularTagListForPeriod(int period);

    /**
     * 전체 태그 상위 8개 리스트
     */
    List<Object[]> getLimitPopularTagList();

    /**
     * 지정된 기간동안 인기태그 리스트
     */
    List<Object[]> getAllPopularTagListForPeriod(int period);

}
