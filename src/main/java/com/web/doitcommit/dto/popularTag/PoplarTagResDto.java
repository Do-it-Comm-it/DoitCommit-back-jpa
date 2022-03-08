package com.web.doitcommit.dto.popularTag;

import lombok.Data;

@Data
public class PoplarTagResDto {

    private String tag;

    private int count;

    public PoplarTagResDto(String tag, int count) {
        this.tag = tag;
        this.count = count;
    }
}
