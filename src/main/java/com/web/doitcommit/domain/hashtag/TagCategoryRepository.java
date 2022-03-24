package com.web.doitcommit.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

public interface TagCategoryRepository extends JpaRepository<TagCategory, Long>, TagCategoryRepositoryQuerydsl {

}
