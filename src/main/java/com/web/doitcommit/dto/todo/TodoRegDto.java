package com.web.doitcommit.dto.todo;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoRegDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String type;

    @NotBlank
    private String importance;

    @NotBlank
    private Boolean isFixed;

    private LocalDateTime todoDateTime;

    public Todo toEntity(Long principalId){

        return Todo.builder()
                .member(Member.builder().memberId(principalId).build())
                .title(title)
                .content(content)
                .type(TodoType.valueOf(type.toUpperCase()))
                .importance(Importance.valueOf(importance.toUpperCase()))
                .isFixed(isFixed)
                .build();
    }
}
