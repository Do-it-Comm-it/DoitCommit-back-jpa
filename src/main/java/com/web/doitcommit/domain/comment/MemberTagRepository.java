package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MemberTagRepository extends JpaRepository<MemberTag,Long> {

    Set<MemberTag> findByComment(Comment comment);
}
