package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ManyToOne;
import java.util.Set;

public interface MemberTagRepository extends JpaRepository<MemberTag,Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from MemberTag mt where mt.comment = :comment")
    void deleteByComment(@Param("comment") Comment comment);

    //테스트 코드
    Set<MemberTag> findByComment(Comment comment);
}
