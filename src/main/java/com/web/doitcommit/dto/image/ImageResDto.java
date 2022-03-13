package com.web.doitcommit.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageResDto {

    private String filePath;
    private String fileNm;

    public ImageResDto(String filePath, String fileNm) {
        if (filePath != null && fileNm != null) {
            this.filePath = filePath;
            this.fileNm = fileNm;
        }
    }

}
