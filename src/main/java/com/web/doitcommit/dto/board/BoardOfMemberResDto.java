package com.web.doitcommit.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "회원별 - 작성한 게시글 dto")
@Getter
public class BoardOfMemberResDto {

    private Long boardId;

    private String title;

}
