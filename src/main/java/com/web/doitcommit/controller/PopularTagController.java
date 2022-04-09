package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.popularTag.PoplarTagResDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.service.popularTag.PopularTagService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PopularTagController {

    private final PopularTagService popularTagService;

    /**
     * 7일간의 {tagId, tagName, count} 인기 태그 리스트
     */
    @Operation(summary = "인기태그 리스트 조회 api", description = "7일간의 인기태그 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PoplarTagResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/popularTags")
    public ResponseEntity<?> getAllPopularTagList() {
        List<PoplarTagResDto> popularTagResDtoList = popularTagService.getAllPopularTagList();

        return new ResponseEntity<>(new CMRespDto<>(
                1, "인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
    }

    /**
     * 7일간의 {tagId, tagName, count} 상위 8개 인기태그 리스트 조회 - redis 사용
     */
    @Operation(summary = "상위 8개의 인기태그 리스트 조회 api", description = "7일간의 상위 8개 인기태그를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PoplarTagResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/popularTags/limit")
    public ResponseEntity<?> getLimitPopularTagList() {
        List<PoplarTagResDto> popularTagResDtoList = popularTagService.getLimitPopularTagList();

        return new ResponseEntity<>(new CMRespDto<>(
                1, "상위 8개 인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
    }
}


