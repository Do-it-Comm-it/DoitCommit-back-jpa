package com.web.doitcommit.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDto {

    @Schema(description = "s3로 반환해준 전체 이미지 배열", nullable = true)
    private ImageRegDto[] allImageArr;

    @Schema(description = "실제 등록하는 이미지 배열", nullable = true)
    private ImageRegDto[] imageArr;

    @Schema(description = "기존 글에서 삭제된 이미지 id 리스트", nullable = true)
    private List<Long> deletedImageArr;
}
