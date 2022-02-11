package com.web.doitcommit.service.member;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.member.StateType;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.member.MemberUpdateDto;
import com.web.doitcommit.commons.FileHandler;
import com.web.doitcommit.handler.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    /**
     * 멤버 정보조회
     */
    public MemberInfoDto reqGetMemberInfo(Long memberId) {
        try {
            Member member = memberRepository.findByMemberId(memberId);
            MemberInfoDto memberInfo = new MemberInfoDto(member);
            return memberInfo;
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않은 회원입니다.");
        }
    }

    /**
     * 멤버 닉네임 중복 체크
     */
    public Boolean reqGetMemberCheck(String nickname) {
        System.out.println(nickname);
        if (!nickname.isEmpty() && (memberRepository.mNicknameCount(nickname) == 0)) {
            return true;
        }
        return false;
    }

    /**
     * 멤버 정보 수정
     */
    @Transactional
    public Boolean reqPutMemberUpdate(MemberUpdateDto memberUpdateDto, Long memberId) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));

        MultipartFile file = memberUpdateDto.getFile();
        if (file != null) {
            memberEntity.changePictureUrl(fileHandler.fileUpload(file));
        }

        memberEntity.changeNickname(memberUpdateDto.getNickname());
        memberEntity.changeEmail(memberUpdateDto.getEmail());
        memberEntity.changePosition(memberUpdateDto.getPosition());
        memberEntity.changeInterestTechSet(memberUpdateDto.getInterestTechSet());
        memberEntity.changeGithubUrl(memberUpdateDto.getGithubUrl());
        memberEntity.changeUrl1(memberUpdateDto.getUrl1());
        memberEntity.changeUrl2(memberUpdateDto.getUrl2());

        return true;
    }

    /**
     * 멤버 탈퇴
     */
    @Transactional
    public Boolean reqPutMemberLeave(Long memberId) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));
        Date date = new Date();
        LocalDateTime localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        memberEntity.changeState(StateType.deactivate);
        memberEntity.changeLeaveDate(localDate);
        return true;
    }

    /**
     * 멤버 탈퇴해제
     */
    @Transactional
    public Boolean reqPutMemberLeaveCancel(Long memberId) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));
        memberEntity.changeState(StateType.activate);
        memberEntity.changeLeaveDate(null);
        return true;
    }
}
