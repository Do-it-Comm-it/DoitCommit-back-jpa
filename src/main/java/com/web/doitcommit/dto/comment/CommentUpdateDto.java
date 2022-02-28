package com.web.doitcommit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {

    @NotNull
    private Long commentId;

    @NotNull
    private String content;

}
