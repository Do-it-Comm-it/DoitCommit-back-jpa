package com.web.doitcommit.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class ImageRegDto {

    private String fileNm;
    private String filePath;

    public ImageRegDto(){}

    public ImageRegDto(String fileNm, String filePath){
        this.fileNm = fileNm;
        this.filePath = filePath;

    }
}
