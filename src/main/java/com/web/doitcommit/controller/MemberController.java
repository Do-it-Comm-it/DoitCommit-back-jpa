package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test(){
        return "성공";
    }

    @GetMapping("/info")
    public ResponseEntity<?> reqGetMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.reqGetMemberInfo(principalDetails.getMember().getMemberId());
        return new ResponseEntity<>(new CMRespDto<>(1, "멤버 조회 성공", member), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<?> reqGetMemberCheck(@RequestParam String nickname) {
        Boolean result = memberService.reqGetMemberCheck(nickname);
        return new ResponseEntity<>(new CMRespDto<>(1, "닉네임 중복 조회 성공", result), HttpStatus.OK);
    }
}
