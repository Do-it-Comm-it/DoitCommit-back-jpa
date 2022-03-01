package com.web.doitcommit.dto.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegDto {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

    private Long parentId;

    public Comment toEntity(Board board, Long principalId, Comment parent){
        Comment comment = Comment.builder()
                .member(Member.builder().memberId(principalId).build())
                .content(content)
                .isExist(true)
                .build();
        comment.setBoard(board);
        comment.setParent(parent);

        return comment;
    }

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
