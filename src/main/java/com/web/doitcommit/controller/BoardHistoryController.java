package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.board.BoardListResDto;
import com.web.doitcommit.dto.page.PageRequestDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import com.web.doitcommit.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardHistoryController {

    private final BoardService boardService;

    /**
     * 회원별 - 게시글 조회 히스토리 내역 조회
     */
    @Operation(summary = "게시판 히스토리 내역 조회 API", description = "회원별 게시판 조회 히스토리 내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BoardListResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/boards/history")
    public ResponseEntity<?> getBoardHistoryList(PageRequestDto pageRequestDto,
                                                 @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        ScrollResultDto<BoardListResDto, Object[]> boardHistoryList =
                boardService.getBoardHistoryList(pageRequestDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 히스토리 내역 조회", boardHistoryList), HttpStatus.OK);
    }

}
