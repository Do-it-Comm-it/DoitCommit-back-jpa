package com.web.doitcommit.dto.todo;

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
public class TodoUpdateDto {

    @NotNull
    private Long todoId;

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

    @NotBlank
    private Boolean isFinished;

    //TODO - controller 에서 dto 생성시 기본값 세팅되는지 체크해야함.
    @NotBlank
    private LocalDateTime todoDateTime = LocalDateTime.now();
}