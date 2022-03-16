package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @Operation(summary = "댓글 등록 API", description = "댓글을 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"댓글등록 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentRegDto commentRegDto, BindingResult bindingResult,
                                           @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDetails principalDetails){

        commentService.register(commentRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 댓글 삭제
     */
    @Operation(summary = "댓글 삭제 API", description = "댓글의 IsExist를 false로 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"댓글삭제 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PatchMapping("/comments/{commentId}/isExist")
    public ResponseEntity<?> removeComment(@PathVariable("commentId") Long commentId){

            commentService.remove(commentId);

            return new ResponseEntity<>(new CMRespDto<>(1,"댓글삭제 성공",null), HttpStatus.OK);
    }

}
