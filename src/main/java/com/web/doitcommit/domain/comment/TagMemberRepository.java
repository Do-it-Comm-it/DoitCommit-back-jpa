package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagMemberRepository extends JpaRepository<TagMember,Long> {

    Set<TagMember> findByComment(Comment comment);
}
