package com.web.doitcommit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {

    @NotNull
    private Long commentId;

    @NotNull
    private String content;

    private Set<Long> memberIdSet;

}
