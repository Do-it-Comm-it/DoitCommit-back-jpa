package com.web.doitcommit.controller;

import com.web.doitcommit.dto.MemberDto;
import com.web.doitcommit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    public MemberDto reqGetMemberInfo(HttpServletRequest request) {
        return memberService.reqGetMemberInfo(request);
    }
}
