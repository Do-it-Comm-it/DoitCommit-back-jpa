package com.web.doitcommit.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageForEditorRegDto {

    @Schema(description = "s3로 반환해준 전체 이미지 리스트", nullable = true)
    private List<ImageRegDto> allImageList = new ArrayList<>();

    @Schema(description = "실제 등록하는 이미지 리스트", nullable = true)
    private List<ImageRegDto> imageList = new ArrayList<>();

    @Schema(description = "기존 글에서 삭제된 이미지 id 리스트", nullable = true)
    private List<Long> deletedImageList = new ArrayList<>();
}
