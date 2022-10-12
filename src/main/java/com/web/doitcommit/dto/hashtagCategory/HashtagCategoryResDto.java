package com.web.doitcommit.dto.hashtagCategory;

import com.web.doitcommit.domain.hashtagCategory.HashtagCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class HashtagCategoryResDto {

    @Schema(description = "태그아이디")
    private Long tagId;

    @Schema(description = "태그명")
    private String tagName;

    public HashtagCategoryResDto(HashtagCategory hashtagCategory){
        tagId = hashtagCategory.getHashtagId();
        tagName = hashtagCategory.getTagName();
    }
}
