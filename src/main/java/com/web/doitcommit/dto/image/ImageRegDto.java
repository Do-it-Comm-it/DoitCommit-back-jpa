package com.web.doitcommit.dto.image;

import lombok.Getter;

@Getter
public class ImageRegDto {

    private String fileNm;
    private String filePath;

    public ImageRegDto(){}

    public ImageRegDto(String fileNm, String filePath){
        this.fileNm = fileNm;
        this.filePath = filePath;

    }
}
