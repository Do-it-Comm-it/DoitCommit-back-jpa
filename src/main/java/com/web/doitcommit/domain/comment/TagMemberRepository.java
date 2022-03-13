package com.web.doitcommit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ManyToOne;
import java.util.Set;

public interface TagMemberRepository extends JpaRepository<TagMember,Long> {

    Set<TagMember> findByComment(Comment comment);

    @Modifying
    @Query("delete from TagMember tm where tm.member.memberId in :memberIds")
    void deleteAllByMemberIdInQuery(@Param("memberIds") Set<Long> memberIdSet);


}
