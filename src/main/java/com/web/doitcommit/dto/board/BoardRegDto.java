package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Schema(description = "게시글 등록 dto")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRegDto {

    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "카테고리 아이디")
    @NotBlank
    private String categoryId;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "글제목")
    @NotBlank
    private String boardTitle;

    @Schema(description = "내용")
    @NotBlank
    private String boardContent;

    @Schema(description = "내용")
    private String thumbnail;

    @Schema(description = "조회수")
    private int boardCnt;

    public Board toEntity(Long principalId){
        Board board = Board.builder()
                .member(Member.builder().memberId(principalId).build())
                .categoryId(categoryId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .thumbnail(thumbnail)
                .build();
        return board;
    }
}
