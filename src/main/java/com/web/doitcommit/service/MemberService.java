package com.web.doitcommit.service;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoDto reqGetMemberInfo(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));
        MemberInfoDto memberInfo = new MemberInfoDto(member);

        return memberInfo;
    }

    public Boolean reqGetMemberCheck(String nickname) {
        int count = memberRepository.mNicknameCount(nickname);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Transactional
    public Boolean reqPutMemberUpdate(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberUpdateDto.getMemberId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));

        member.changeNickname(memberUpdateDto.getNickname());
        member.changeEmail(memberUpdateDto.getEmail());
        member.changeInterestTechSet(memberUpdateDto.getInterestTechSet());
        member.changeGithubUrl(memberUpdateDto.getGithubUrl());
        member.changeUrl1(memberUpdateDto.getUrl1());
        member.changeUrl2(memberUpdateDto.getUrl2());
        member.changePictureUrl(memberUpdateDto.getPictureUrl());
        return true;
    }



}
