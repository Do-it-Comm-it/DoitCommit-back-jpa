package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.comment.CommentListDto;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import com.web.doitcommit.dto.page.PageRequestDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 리스트 조회
     */
    @Operation(summary = "댓글 리스트 조회 API", description = "1.댓글 수, 2.댓글 리스트, 3.회원태그 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommentListDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<?> getTodo(@PathVariable("boardId") Long boardId,
                                     @Parameter(hidden = true) PageRequestDto pageRequestDto) {

        CommentListDto commentListDto = commentService.getCommentList(boardId, pageRequestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글 리스트 조회 성공", commentListDto),HttpStatus.OK);
    }

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
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentRegDto commentRegDto, BindingResult bindingResult,
                                           @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDetails principalDetails){

        commentService.register(commentRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     */
    @Operation(summary = "댓글 수정 API", description = "댓글을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"댓글수정 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> modify(@Valid @RequestBody CommentUpdateDto commentUpdateDto, BindingResult bindingResult){

        commentService.modify(commentUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글수정 성공",null), HttpStatus.OK);
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
