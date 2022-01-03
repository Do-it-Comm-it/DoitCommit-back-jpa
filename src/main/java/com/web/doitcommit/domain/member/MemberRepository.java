package com.web.doitcommit.domain.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"interestTechSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByOauthId(String oauthId);

}
