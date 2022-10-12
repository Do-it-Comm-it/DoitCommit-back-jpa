package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.image.ImageForEditorRegDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 등록 dto")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRegDto {

    @Schema(description = "카테고리 아이디")
    private Long categoryId;

    @Schema(description = "글제목")
    @NotBlank
    private String boardTitle;

    @Schema(description = "내용")
    @NotBlank
    private String boardContent;

    @Schema(description = "태그", nullable = true)
    private List<Long> boardHashtag;

    @Schema(description = "에디터에서 가지고온 이미지", nullable = true)
    private ImageForEditorRegDto imageForEditorRegDto;

    public Board toEntity(Long principalId){
        Board board = Board.builder()
                .boardCategory(BoardCategory.builder().categoryId(categoryId).build())
                .member(Member.builder().memberId(principalId).build())
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardImageList(new ArrayList<>())
                .boardHashtag(new ArrayList<>())
                .build();
        return board;
    }
}
