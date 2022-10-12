package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.hashtagCategory.HashtagCategoryResDto;
import com.web.doitcommit.dto.hashtagCategory.PopularTagResDto;
import com.web.doitcommit.service.hashtagCategory.HashtagCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class HashtagCategoryController {

    private final HashtagCategoryService hashtagCategoryService;

    /**
     * 해시태그 목록 조회
     */
    @Operation(summary = "해시태그 목록 조회 API", description = "해시태그 목록 조회을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = HashtagCategoryResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/hashtags")
    public ResponseEntity<?> getTagList() {
        List<HashtagCategoryResDto> hashtagCategoryResDto = hashtagCategoryService.getBoardTagList();
        return new ResponseEntity<>(new CMRespDto<>(1, "태그 리스트 조회 성공", hashtagCategoryResDto),HttpStatus.OK);
    }

    /**
     * 지정기간 동안 {tagId, tagName, count} 전체 인기 태그 리스트
     */
    @Operation(summary = "인기태그 리스트 조회 api", description = "지정기간 동안 인기태그 리스트를 조회한다. default : 7일")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PopularTagResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/popularTags")
    public ResponseEntity<?> getAllPopularTagList(@RequestParam(value = "period", required = false, defaultValue = "7") int period) {
        List<PopularTagResDto> popularTagResDtoList = hashtagCategoryService.getAllPopularTagListForPeriod(period);

        return new ResponseEntity<>(new CMRespDto<>(
                1, "지정된 기간동안의 인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
    }

//    /**
//     * 전체 {tagId, tagName, count} 상위 8개 인기태그 리스트 조회 - db 조회
//     */
//    @Operation(summary = "전체 기간 상위 8개의 인기태그 리스트 조회 api", description = "전체 상위 8개 인기태그를 조회한다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PoplarTagResDto.class)))),
//            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
//            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
//            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
//            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
//    })
//    @GetMapping("/popularTags/limit")
//    public ResponseEntity<?> getLimitPopularTagList() {
//        List<PoplarTagResDto> popularTagResDtoList = popularTagService.getLimitPopularTagList();
//
//        return new ResponseEntity<>(new CMRespDto<>(
//                1, "전체 기간 상위 8개 인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
//    }

    /**
     * 7일 동안의 {tagId, tagName, count} 상위 8개 인기태그 리스트 조회 - redis 조회
     * -> 빈값이라면
     * 전체 {tagId, tagName, count} 상위 8개 인기태그 리스트 조회 - db 조회
     */
    @Operation(summary = "7일동안 상위 8개의 인기태그 리스트 조회 api", description = "7일 동안의 상위 8개 인기태그를 조회한다. -> 7일동안 쌓이 데이터가 없으면 " +
            "전체 기간 상위 8개의 인기태그 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PopularTagResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/popularTags/limit")
    public ResponseEntity<?> getLimitPopularTagListFor7Days() {
        List<PopularTagResDto> popularTagResDtoListByRedis = hashtagCategoryService.getLimitPopularTagListFor7Days();

        //redis 조회 시 빈값 체크
        //빈값이라면 전체 데이터 중 상위 8개 인기태그 불러오기
        if (CollectionUtils.isEmpty(popularTagResDtoListByRedis)){
            List<PopularTagResDto> popularTagResDtoList = hashtagCategoryService.getLimitPopularTagList();

            return new ResponseEntity<>(new CMRespDto<>(
                    1, "전체 기간 상위 8개 인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
        }

        return new ResponseEntity<>(new CMRespDto<>(
                1, "7일 동안의 상위 8개 인기태그 리스트 불러오기 성공", popularTagResDtoListByRedis ), HttpStatus.OK);
    }
}


