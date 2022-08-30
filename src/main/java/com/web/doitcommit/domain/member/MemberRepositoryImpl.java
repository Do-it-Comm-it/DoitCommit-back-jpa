package com.web.doitcommit.domain.member;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.doitcommit.domain.interestTech.QInterestTech;
import com.web.doitcommit.domain.interestTech.QMemberInterestTech;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 관심기술 목록 조회
     */
    @Override
    public List getCustomInterestTechList(Long memberId) {
        QInterestTech interestTech = QInterestTech.interestTech;
        QMemberInterestTech memberInterestTech = QMemberInterestTech.memberInterestTech1;

        List<Tuple> results = queryFactory
                .select(interestTech.interestTechId, interestTech.interestTechNm)
                .from(interestTech)
                .leftJoin(memberInterestTech).on(interestTech.interestTechId.eq(memberInterestTech.interestTech.interestTechId))
                .where(memberInterestTech.member.memberId.eq(memberId))
                .fetch();

        List interestTechList = new ArrayList();
        for (Tuple result : results) {
            Map<Long,String> interestTechMap = new HashMap<>();
            interestTechMap.put(result.get(interestTech.interestTechId), result.get(interestTech.interestTechNm));
            interestTechList.add(interestTechMap);
        }

        return interestTechList;
    }
}
