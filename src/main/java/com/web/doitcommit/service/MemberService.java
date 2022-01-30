package com.web.doitcommit.service;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Boolean reqPutMemberUpdate(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberUpdateDto.getMemberId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));
        //날짜폴더 경로
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date); //오늘날짜를 포맷함

        String datePath = str.replace("-", File.separator);

        /* 폴더 생성 */
        File uploadPath = new File(uploadFolder, datePath);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String imageFileName = uploadFolder+uuid+"_"+memberUpdateDto.getFile().getOriginalFilename();

        Path imageFilePath = Paths.get(imageFileName);
        //통신, IO 예외가 발생할수 있다.
        try {
            Files.write(imageFilePath, memberUpdateDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // 참고 :  Image 엔티티에 Tag는 주인이 아니다. Image 엔티티로 통해서 Tag를 save할 수 없다.
//
//        // 1. Image 저장
//        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
//        imageRepository.save(image);

        member.changeNickname(memberUpdateDto.getNickname());
        member.changeEmail(memberUpdateDto.getEmail());
        member.changeInterestTechSet(memberUpdateDto.getInterestTechSet());
        member.changeGithubUrl(memberUpdateDto.getGithubUrl());
        member.changeUrl1(memberUpdateDto.getUrl1());
        member.changeUrl2(memberUpdateDto.getUrl2());
        member.changePictureUrl(imageFileName);
        return true;
    }



}
