package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.hashtag.HashtagCategory;
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
