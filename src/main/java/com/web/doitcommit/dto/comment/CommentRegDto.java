package com.web.doitcommit.dto.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegDto {

    @Schema(description = "게시글 고유값")
    @NotNull
    private Long boardId;

    @Schema(description = "본문")
    @NotBlank
    private String content;

    @Schema(description = "회원태그 - 회원 고유값 집합")
    @Builder.Default
    private Set<Long> memberIdSet = new HashSet<>();

    public Comment toEntity(Board board, Long principalId){
        Comment comment = Comment.builder()
                .member(Member.builder().memberId(principalId).build())
                .content(content)
                .isExist(true)
                .build();
        comment.setBoard(board);

        return comment;
    }

}
