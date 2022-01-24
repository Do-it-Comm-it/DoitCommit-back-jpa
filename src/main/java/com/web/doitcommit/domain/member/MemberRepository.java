package com.web.doitcommit.domain.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"interestTechSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByOauthId(String oauthId);

    @Query(value = "SELECT count(*) FROM member WHERE nickname = :nickname", nativeQuery = true)
    int mNicknameCount(String nickname);

}
