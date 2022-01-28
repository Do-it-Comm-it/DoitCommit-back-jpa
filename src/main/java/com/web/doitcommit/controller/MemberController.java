package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 정보 조회 API", description = "멤버 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MemberInfoDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/info")
    public ResponseEntity<?> reqGetMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberInfoDto member = memberService.reqGetMemberInfo(principalDetails.getMember().getMemberId());
        return new ResponseEntity<>(new CMRespDto<>(1, "멤버 조회 성공", member), HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복 체크 API", description = "닉네임 중복체크를 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"닉네임 중복 조회 성공\",\n" +
                    "  \"data\": true,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/check")
    public ResponseEntity<?> reqGetMemberCheck(@RequestParam String nickname) {
        Boolean result = memberService.reqGetMemberCheck(nickname);
        return new ResponseEntity<>(new CMRespDto<>(1, "닉네임 중복 조회 성공", result), HttpStatus.OK);
    }

    /*@PutMapping(value ="/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> reqPutMemberUpdate(@RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.reqPutMemberUpdate(memberUpdateDto);
        return new ResponseEntity<>(new CMRespDto<>(1, "회원 수정 성공", null), HttpStatus.OK);
    }*/
}
