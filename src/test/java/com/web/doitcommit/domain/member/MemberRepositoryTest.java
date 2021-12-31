package com.web.doitcommit.domain.member;

import com.web.doitcommit.domain.interestTech.InterestTech;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void findByOauthId() throws Exception{
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("1111")
                .username("testUsername")
                .interestTechSet(new HashSet<>(Arrays.asList(InterestTech.Java)))
                .oauthId("testId")
                .build();

        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByOauthId("testId").get();

        //then
        System.out.println(findMember);
        Assertions.assertThat(findMember).isEqualTo(member);
    }

}