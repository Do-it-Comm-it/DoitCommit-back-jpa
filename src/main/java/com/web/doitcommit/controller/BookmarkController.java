package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.service.bookmark.BookmarkService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 추가
     */
    @Operation(summary = "게시글 북마크 추가 API", description = "게시글을 북마크에 추가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"북마크 추가 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping("/boards/{boardId}/bookmarks")
    public ResponseEntity<?> createBookmark(@PathVariable("boardId") Long boardId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.createBookmark(principalDetails.getMember().getMemberId(), boardId);

        return new ResponseEntity<>(new CMRespDto<>(1,"북마크 추가 성공",null), HttpStatus.CREATED);
    }

    /**
     * 북마크 취소
     */
    @Operation(summary = "게시글 북마크 취소 API", description = "북마크에서 게시글을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"북마크 취소하기 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @DeleteMapping("/boards/{boardId}/bookmarks")
    public ResponseEntity<?> cancelBookmark(@PathVariable("boardId") Long boardId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.cancelBookmark(principalDetails.getMember().getMemberId(), boardId);

        return new ResponseEntity<>(new CMRespDto<>(1, "북마크 취소하기 성공",null),HttpStatus.OK);
    }

}
