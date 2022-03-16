package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ManyToOne;
import java.util.Set;

public interface MemberTagRepository extends JpaRepository<MemberTag,Long> {

    Set<MemberTag> findByComment(Comment comment);

    @Modifying
    @Query("delete from MemberTag mt where mt.member.memberId in :memberIds")
    void deleteAllByMemberIdInQuery(@Param("memberIds") Set<Long> memberIdSet);


}
