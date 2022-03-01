package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.service.heart.HeartService;
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
public class HeartController {

    private final HeartService heartService;

    /**
     * 좋아요 생성
     */
    @Operation(summary = "게시글 좋아요 API", description = "좋아요를 추가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"좋아요 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping("/boards/{boardId}/hearts")
    public ResponseEntity<?> heart(@PathVariable("boardId") Long boardId,
                                   @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        heartService.heart(principalDetails.getMember().getMemberId(), boardId);

        return new ResponseEntity<>(new CMRespDto<>(1,"좋아요 성공",null), HttpStatus.CREATED);
    }

    /**
     * 좋아요 취소
     */
    @Operation(summary = "게시글 좋아요 취소 API", description = "좋아요를 취소한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"좋아요 취소 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @DeleteMapping("/boards/{boardId}/hearts")
    public ResponseEntity<?> cancelHeart(@PathVariable("boardId") Long boardId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        heartService.cancelHeart(principalDetails.getMember().getMemberId(),boardId);

        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공",null),HttpStatus.OK);
    }


}
