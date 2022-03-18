package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.hashtag.TagCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TagCategoryResDto {

    @Schema(description = "태그아이디")
    private Long tagId;

    @Schema(description = "태그명")
    private String tagName;

    public TagCategoryResDto(TagCategory tagCategory){
        tagId = tagCategory.getTagId();
        tagName = tagCategory.getTagName();
    }
}
