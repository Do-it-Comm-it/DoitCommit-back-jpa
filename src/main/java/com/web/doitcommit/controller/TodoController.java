package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.member.MemberInfoDto;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;
import com.web.doitcommit.service.todo.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/todos")
@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;

    /**
     * 메인화면 투두리스트 조회
     */
    @Operation(summary = "메인화면 - 투두리스트 조회 API", description = "투두리스트 상단 4개를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TodoResDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/main")
    public ResponseEntity<?> getCustomLimitTodoList(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        List<TodoResDto> todoResDtoList =
                todoService.getCustomLimitTodoList(4, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "메인화면 투두리스트 가져오기 성공", todoResDtoList),HttpStatus.OK);
    }


    /**
     * 투두 생성
     */
    @Operation(summary = "투두 생성 API", description = "todo를 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"투두생성 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoRegDto todoRegDto, BindingResult bindingResult,
                                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails){

        todoService.register(todoRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"투두생성 성공",null), HttpStatus.CREATED);
    }


    /**
     * 투두 수정
     */
    @Operation(summary = "투두 수정 API", description = "todo를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"투두수정 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PutMapping("/{todoId}")
    public ResponseEntity<?> modifyTodo(@Valid @RequestBody TodoUpdateDto todoUpdateDto, BindingResult bindingResult){

        todoService.modify(todoUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두수정 성공", null), HttpStatus.OK);
    }


    /**
     * 투두 완료체크 수정
     */
    @Operation(summary = "투두 완료체크 수정 API", description = "투두 완료체크를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"투두 완료체크 수정 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PatchMapping("/{todoId}/finished")
    public ResponseEntity<?> modifyIsFinished(@PathVariable("todoId") Long todoId){
        todoService.modifyIsFinished(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 완료체크 수정 성공", null), HttpStatus.OK);
    }


    /**
     * 투두 상단고정 수정
     */
    @Operation(summary = "투두 상단고정 수정 API", description = "투두 상단고정을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"투두 상단고정 수정 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PatchMapping("/{todoId}/fixed")
    public ResponseEntity<?> modifyIsFixed(@PathVariable("todoId") Long todoId){
        todoService.modifyIsFixed(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 상단고정 수정 성공", null), HttpStatus.OK);
    }


    /**
     * 투두 삭제
     */
    @Operation(summary = "투두 삭제 API", description = "todo를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"투두 삭제 성공 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable("todoId") Long todoId){
        todoService.remove(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 삭제 성공 성공", null), HttpStatus.OK);
    }
}
