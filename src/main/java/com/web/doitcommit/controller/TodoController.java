package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;
import com.web.doitcommit.service.todo.TodoService;
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
    @GetMapping("/main")
    public ResponseEntity<?> getCustomLimitTodoList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<TodoResDto> todoResDtoList =
                todoService.getCustomLimitTodoList(4, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "메인화면 투두리스트 가져오기 성공", todoResDtoList),HttpStatus.OK);
    }


    /**
     * 투두 생성
     */
    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoRegDto todoRegDto, BindingResult bindingResult,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails){

        todoService.register(todoRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"투두생성 성공",null), HttpStatus.CREATED);
    }

    /**
     * 투두 수정
     */
    @PutMapping("/{todoId}")
    public ResponseEntity<?> modifyTodo(@Valid @RequestBody TodoUpdateDto todoUpdateDto, BindingResult bindingResult){

        todoService.modify(todoUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두수정 성공", null), HttpStatus.OK);
    }

    /**
     * 투두 완료체크 수정
     */
    @PatchMapping("/{todoId}/finished")
    public ResponseEntity<?> modifyIsFinished(@PathVariable("todoId") Long todoId){
        todoService.modifyIsFinished(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 완료체크 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 투두 상단고정 수정
     */
    @PatchMapping("/{todoId}/fixed")
    public ResponseEntity<?> modifyIsFixed(@PathVariable("todoId") Long todoId){
        todoService.modifyIsFixed(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 상단고정 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 투두 삭제
     */
    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable("todoId") Long todoId){
        todoService.remove(todoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "투두 삭제 성공 성공", null), HttpStatus.OK);
    }
}
