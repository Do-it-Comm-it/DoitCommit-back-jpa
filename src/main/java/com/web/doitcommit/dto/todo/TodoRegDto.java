package com.web.doitcommit.dto.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private Boolean isFixed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd'T'HH:mm", timezone = "Asia/Seoul")
    @Builder.Default //테스트코드에 사용
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