package com.web.doitcommit.dto.popularTag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PoplarTagResDto {

    @Schema(description = "태그이름")
    private String tag;

    @Schema(description = "태그 수")
    private int count;

    public PoplarTagResDto(String tag, int count) {
        this.tag = tag;
        this.count = count;
    }
}
