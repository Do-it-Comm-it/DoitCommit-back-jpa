package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.board.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;


    /**
     * 메인화면 게시글 리스트 조회
     */
    @Operation(summary = "메인화면 게시글 리스트 조회 API", description = "커뮤니티 최신글 4개를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MainViewBoardListResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/list/main")
    public ResponseEntity<?> getCustomLimitBoardList(@RequestParam(value = "limit", required = false, defaultValue = "4") int limit,
                                                     @RequestParam(value = "order", required = false, defaultValue = "boardId") String order,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<MainViewBoardListResDto> mainViewBoardListResDtoList;

        //비로그인 상태
        if(principalDetails == null){
            mainViewBoardListResDtoList = boardService.getCustomLimitBoardList(limit, order,null);
        }
        //로그인 상태
        else{
            mainViewBoardListResDtoList = boardService.getCustomLimitBoardList(limit, order, principalDetails.getMember().getMemberId());
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "메인화면 게시글 리스트 조회 성공", mainViewBoardListResDtoList),HttpStatus.OK);
    }


    /**
     * 게시판 목록 조회
     */
    @Operation(summary = "게시판 목록 조회 API", description = "게시판 목록 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BoardListResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/list")
    public ResponseEntity<?> getBoardList(PageRequestDto pageRequestDto,
                                          @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ScrollResultDto<BoardListResDto, Object[]> boardListResDtoList;

        //비로그인 상태
        if(principalDetails == null){
            boardListResDtoList = boardService.getBoardList(pageRequestDto, null);
        }
        //로그인 상태
        else{
            boardListResDtoList = boardService.getBoardList(pageRequestDto, principalDetails.getMember().getMemberId());
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "게시판 리스트 조회 성공", boardListResDtoList),HttpStatus.OK);
    }

    /**
     * 회원별 - 작성한 게시글 리스트 사용자 개수 지정 조회
     */
    @GetMapping("/members/{memberId}/limit")
    public ResponseEntity<?> getCustomLimitBoardListOfMember(@PathVariable("memberId") Long memberId,
                                                             @RequestParam(value = "limit", required = false, defaultValue = "2") int limit){



    }

    /**
     * 게시글 등록
     */
    @Operation(summary = "게시글 등록 API", description = "게시글을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"게시글 등록 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping("")
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardRegDto boardRegDto, BindingResult bindingResult,
                                         @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        boardService.createBoard(boardRegDto, principalDetails.getMember().getMemberId());
        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 등록 성공", null),HttpStatus.CREATED);
    }

    /**
     * 게시글 조회
     */
    @Operation(summary = "게시글 조회 API", description = "게시글을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("")
    public ResponseEntity<?> GetBoard(@Parameter(name = "boardId") Long boardId, @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails) {

        BoardResDto boardResDto;

        //비로그인 상태
        if(principalDetails == null){
            boardResDto = boardService.GetBoard(boardId, null);
        }
        else{
            boardResDto = boardService.GetBoard(boardId, principalDetails.getMember().getMemberId());
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 조회 성공", boardResDto), HttpStatus.OK);
    }

    /**
     * 태그 목록 조회
     */
    @Operation(summary = "태그 목록 조회 API", description = "태그 목록 조회을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagCategoryResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/tag")
    public ResponseEntity<?> getTagList() {
        List<TagCategoryResDto> tagCategoryResDto = boardService.getBoardTagList();
        return new ResponseEntity<>(new CMRespDto<>(1, "태그 리스트 조회 성공", tagCategoryResDto),HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     */
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"게시글 삭제 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> removeBoard(@Parameter(name = "boardId") @PathVariable("boardId") Long boardId) {
        boardService.remove(boardId);
        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 삭제 성공", null), HttpStatus.OK);
    }

}