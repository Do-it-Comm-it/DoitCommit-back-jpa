package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.hashtag.TagCategory;
import com.web.doitcommit.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Schema(description = "s3로 반환해준 전체 이미지 배열", nullable = true)
    private ImageRegDto[] allImageArr;

    @Schema(description = "실제 등록하는 이미지 배열", nullable = true)
    private ImageRegDto[] imageArr;

    public Board toEntity(Long principalId){
        Board board = Board.builder()
                .boardCategory(BoardCategory.builder().categoryId(categoryId).build())
                .member(Member.builder().memberId(principalId).build())
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardImage(new ArrayList<>())
                .boardHashtag(new ArrayList<>())
                .build();
        return board;
    }
}
