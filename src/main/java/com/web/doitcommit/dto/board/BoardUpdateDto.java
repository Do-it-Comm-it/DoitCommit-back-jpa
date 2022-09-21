package com.web.doitcommit.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDto {

    @NotNull
    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "카테고리 아이디")
    private Long categoryId;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

}
