package com.web.doitcommit.dto.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRegDto {

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "본문")
    @NotBlank
    private String content;

    @Schema(description = "투두타입")
    @NotBlank
    private String type;

    @Schema(description = "중요도")
    @NotBlank
    private String importance;

    @Schema(description = "상단고정 여부")
    @NotNull
    private Boolean isFixed;

    @Schema(description = "투두 날짜('yyyy/MM/ddTHH:mm') - default: 현재날짜 자동 입력", nullable = true)
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