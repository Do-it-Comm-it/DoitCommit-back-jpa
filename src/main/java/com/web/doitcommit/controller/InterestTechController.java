package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.interestTechDto.InterestTechDto;
import com.web.doitcommit.service.InterestTech.InterestTechService;
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
public class InterestTechController {

    private final InterestTechService interestTechService;

    /**
     * 관심분야 목록 조회
     */
    @Operation(summary = "관심분야 목록 조회 api", description = "관심분야 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InterestTechDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/interest/tech")
    public ResponseEntity<?> getInterestTechList() {
        List<InterestTechDto> interestTechDtoList = interestTechService.getInterestTechList();

        return new ResponseEntity<>(new CMRespDto<>(
                1, "관심분야 리스트 불러오기 성공", interestTechDtoList), HttpStatus.OK);
    }
}
