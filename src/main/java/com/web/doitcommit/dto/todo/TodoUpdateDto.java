package com.web.doitcommit.dto.todo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
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
}
