package com.web.doitcommit.dto.popularTag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PoplarTagResDto {

    @Schema(description = "태그 고유값")
    private Long tagId;

    @Schema(description = "태그이름")
    private String tagName;

    @Schema(description = "태그 수")
    private int count;

    public PoplarTagResDto(Long tagId, String tagName, int count) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.count = count;
    }
}
