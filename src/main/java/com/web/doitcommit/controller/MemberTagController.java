package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberTagController {

    private final CommentService commentService;

    /**
     * 회원 태그 리스트 조회
     */
    @Operation(summary = "회원 태그 리스트 조회 api", description = "게시글에 댓글 작성한 회원들의 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberTagResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/memberTags/boards/{boardId}")
    public ResponseEntity<?> getMemberTagList(@PathVariable("boardId") Long boardId){

        List<MemberTagResDto> memberTagResList = commentService.getMemberTagList(boardId);

        return new ResponseEntity<>(new CMRespDto<>(
                1,"회원 태그 리스트 조회 성공", memberTagResList), HttpStatus.OK);
    }

}
