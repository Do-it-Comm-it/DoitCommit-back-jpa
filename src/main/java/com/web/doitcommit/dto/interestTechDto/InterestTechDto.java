package com.web.doitcommit.dto.interestTechDto;

import com.web.doitcommit.domain.interestTech.InterestTech;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InterestTechDto {

    @Schema(description = "관심기술 아이디")
    private Long interestTechId;
    @Schema(description = "관심기술 명")
    private String interestTechNm;

    public InterestTechDto(InterestTech interestTech) {
        interestTechId = interestTech.getInterestTechId();
        interestTechNm = interestTech.getInterestTechNm();
    }
}
