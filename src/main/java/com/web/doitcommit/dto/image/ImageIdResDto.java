package com.web.doitcommit.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageIdResDto {

    @Schema(description = "이미지 Id")
    private Long imageId;

    @Schema(description = "이미지 url")
    private String imageUrl;

    public ImageIdResDto(Long imageId, String imageUrl) {
        if (imageId != null && imageUrl != null) {
            this.imageId = imageId;
            this.imageUrl = imageUrl;
        }
    }
}
