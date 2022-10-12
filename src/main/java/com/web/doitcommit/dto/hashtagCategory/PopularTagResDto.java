package com.web.doitcommit.dto.hashtagCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PopularTagResDto {

    @Schema(description = "태그 고유값")
    private Long tagId;

    @Schema(description = "태그이름")
    private String tagName;

    @Schema(description = "태그 수")
    private int count;

    public PopularTagResDto(Long tagId, String tagName, int count) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.count = count;
    }
}
