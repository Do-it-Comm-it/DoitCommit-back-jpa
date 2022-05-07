package com.web.doitcommit.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;

import static com.web.doitcommit.domain.member.QMember.member;
import static com.web.doitcommit.domain.files.QMemberImage.memberImage;

public class MemberRepositoryImpl implements MemberRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 멤버 이미지 url 조회
     */
    @Override
    public String getMemberImage(Long memberId) {

        String result = queryFactory
                .select(memberImage.imageUrl)
                .from(member)
                .join(memberImage).on(memberImage.member.eq(member))
                .where(member.memberId.eq(memberId))
                .fetchOne();

        return result;
    }
}
