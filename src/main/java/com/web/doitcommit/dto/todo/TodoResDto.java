package com.web.doitcommit.dto.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.todo.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResDto {

    @Schema(description = "todo 고유값")
    private Long todoId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "본문")
    private String content;

    @Schema(description = "투두타입")
    private String type;

    @Schema(description = "중요도")
    private String importance;

    @Schema(description = "상단고정 여부")
    private Boolean isFixed;

    @Schema(description = "완료상태")
    private Boolean isFinished;

    @Schema(description = "투두 날짜('yyyy-MM-ddTHH:mm')")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
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
