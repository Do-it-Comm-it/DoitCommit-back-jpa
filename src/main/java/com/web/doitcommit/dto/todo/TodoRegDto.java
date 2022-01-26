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
@NoArgsConstructor
@AllArgsConstructor
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

    //TODO - controller 에서 dto 생성시 기본값 세팅되는지 체크해야함.
    @Builder.Default //테스트코드에 사용
    @NotBlank
    private LocalDateTime todoDateTime = LocalDateTime.now();

    public Todo toEntity(Long principalId){

        Todo todo = Todo.builder()
                .member(Member.builder().memberId(principalId).build())
                .title(title)
                .content(content)
                .type(TodoType.valueOf(type.toUpperCase()))
                .importance(Importance.valueOf(importance.toUpperCase()))
                .isFixed(isFixed)
                .todoDateTime(todoDateTime)
                .build();

        return todo;
    }
}