package com.web.doitcommit.dto.todo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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

    @NotBlank
    private LocalDateTime todoDateTime = LocalDateTime.now();
}