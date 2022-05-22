package com.web.doitcommit.domain.hashtag;

import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TagCategoryRepositoryTest {

    @Autowired TagCategoryRepository tagCategoryRepository;
    @Autowired MemberRepository memberRepository;


    @Disabled
    @Test
    void 최근_7일_인기태그() throws Exception{
        //given

        //when
        List<Object[]> popularTagList = tagCategoryRepository.getLimitPopularTagListForPeriod(7);

        //then
        for (Object[] object : popularTagList){
            System.out.println(object[0]);
            System.out.println(object[1]);
        }
    }

    private Member createMember(String email, String nickname, String username, String oAuthId) {
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password("1111")
                .username(username)
                .provider(AuthProvider.GOOGLE)
                .interestTechSet(new HashSet<>(Arrays.asList("Java")))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }

}