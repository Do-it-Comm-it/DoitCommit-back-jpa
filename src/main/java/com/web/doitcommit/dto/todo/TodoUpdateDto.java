package com.web.doitcommit.dto.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "todo 고유값")
    @NotNull
    private Long todoId;

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

    @Schema(description = "투두 날짜('yyyy-MM-ddTHH:mm')")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime todoDateTime;
}