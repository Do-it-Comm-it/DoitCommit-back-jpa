package com.web.doitcommit.service.member;

import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.interestTech.MemberInterestTech;
import com.web.doitcommit.domain.interestTech.MemberInterestTechRepository;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.member.StateType;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.member.MemberUpdateDto;
import com.web.doitcommit.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final MemberInterestTechRepository memberInterestTechRepository;

    /**
     * 멤버 정보조회
     */
    public MemberInfoDto reqGetMemberInfo(Long memberId) {
        try {
            Member memberEntity = memberRepository.findByMemberId(memberId);
            MemberInfoDto memberInfo = new MemberInfoDto(memberEntity);

            if(memberRepository.getMemberImage(memberId) != null){
                memberInfo.setPictureUrl(memberRepository.getMemberImage(memberId));
            }

            //관심기술정보 목록 조회
            List interestTechList = memberRepository.getCustomInterestTechList(memberId);

            if (interestTechList.size() != 0) {
                memberInfo.setInterestTechSet(interestTechList);
            }

            return memberInfo;
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않은 회원입니다.");
        }
    }

    /**
     * 멤버 닉네임 중복 체크
     */
    public Boolean reqGetMemberCheck(String nickname) {
        if (!nickname.isEmpty() && (memberRepository.mNicknameCount(nickname) == 0)) {
            return true;
        }
        return false;
    }

    /**
     * 멤버 정보 수정
     */
    @Transactional
    public Boolean reqPutMemberUpdate(MemberUpdateDto memberUpdateDto, Long memberId) throws IOException {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 회원입니다."));

        MultipartFile imageFile = memberUpdateDto.getImageFile();

        //이미지 수정할 경우
        if (imageFile != null) {
            if(memberEntity.getMemberImage() != null){ //기존 파일이 있으면
                imageService.imageRemove(memberEntity.getMemberImage().getImageId()); //파일 테이블에서 삭제
            }
            //새로운 이미지 저장
            imageService.imageMemberRegister(memberEntity, imageFile);
        }

        if(memberUpdateDto.getInterestTechSet() !=null){
            for (int i = 0; i < memberUpdateDto.getInterestTechSet().size(); i++) {
                InterestTech interestTech = new InterestTech(memberUpdateDto.getInterestTechSet().get(i));
                MemberInterestTech memberInterestTech = new MemberInterestTech(memberEntity,interestTech);
                memberInterestTechRepository.save(memberInterestTech);
            }
        }

        memberEntity.changeNickname(memberUpdateDto.getNickname());
        memberEntity.changeEmail(memberUpdateDto.getEmail());
        memberEntity.changePosition(memberUpdateDto.getPosition());
        //memberEntity.changeInterestTechSet(memberUpdateDto.getInterestTechSet());
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
