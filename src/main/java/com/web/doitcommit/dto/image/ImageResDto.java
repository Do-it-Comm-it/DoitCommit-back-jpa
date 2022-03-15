package com.web.doitcommit.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageResDto {

    @Schema(description = "이미지 파일경로")
    private String filePath;
    @Schema(description = "이미지 파일이름")
    private String fileNm;

    public ImageResDto(String filePath, String fileNm) {
        if (filePath != null && fileNm != null) {
            this.filePath = filePath;
            this.fileNm = fileNm;
        }
    }

}
