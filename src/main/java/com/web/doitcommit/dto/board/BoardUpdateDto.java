package com.web.doitcommit.dto.board;

import com.web.doitcommit.dto.image.ImageForEditorRegDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDto {

    @Schema(description = "게시글 번호")
    @NotNull
    private Long boardId;

    @Schema(description = "카테고리 아이디")
    @NotNull
    private Long categoryId;

    @Schema(description = "글제목")
    @NotBlank
    private String boardTitle;

    @Schema(description = "글내용")
    @NotBlank
    private String boardContent;

    @Schema(description = "태그", nullable = true)
    private List<Long> boardHashtag;

    @Schema(description = "에디터에서 가지고온 이미지", nullable = true)
    private ImageForEditorRegDto imageForEditorRegDto;

}
