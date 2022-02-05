package com.web.doitcommit.service.member;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.member.MemberUpdateDto;
import com.web.doitcommit.commons.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    @Value("${file.path}")
    private String uploadFolder;

    public MemberInfoDto reqGetMemberInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
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
    public Boolean reqPutMemberUpdate(MemberUpdateDto memberUpdateDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));

        MultipartFile file = memberUpdateDto.getFile();
        if (file != null) {
            member.changePictureUrl(fileHandler.fileUpload(file));
        }

        member.changeNickname(memberUpdateDto.getNickname());
        member.changeEmail(memberUpdateDto.getEmail());
        member.changeInterestTechSet(memberUpdateDto.getInterestTechSet());
        member.changeGithubUrl(memberUpdateDto.getGithubUrl());
        member.changeUrl1(memberUpdateDto.getUrl1());
        member.changeUrl2(memberUpdateDto.getUrl2());

        return true;
    }


}
