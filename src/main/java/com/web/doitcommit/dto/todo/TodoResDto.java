package com.web.doitcommit.dto.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.todo.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResDto {

    private Long todoId;

    private String title;

    private String content;

    private String type;

    private String importance;

    private Boolean isFixed;

    private Boolean isFinished;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime todoDateTime;

    public TodoResDto(Todo todo){
        todoId = todo.getTodoId();
        title = todo.getTitle();
        content = todo.getContent();
        type = todo.getType().toString();
        importance = todo.getImportance().toString();
        isFixed = todo.getIsFixed();
        isFinished = todo.getIsFinished();
        todoDateTime = todo.getTodoDateTime();
    }
}
