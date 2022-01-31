package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/todos")
@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoRegDto todoRegDto, BindingResult bindingResult,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails){

        todoService.register(todoRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"투두생성 성공",null), HttpStatus.CREATED);
    }
}
