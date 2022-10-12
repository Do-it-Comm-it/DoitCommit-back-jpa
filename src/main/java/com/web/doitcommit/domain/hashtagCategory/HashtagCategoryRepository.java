package com.web.doitcommit.domain.hashtagCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagCategoryRepository extends JpaRepository<HashtagCategory, Long>, HashtagCategoryRepositoryQuerydsl {

}
