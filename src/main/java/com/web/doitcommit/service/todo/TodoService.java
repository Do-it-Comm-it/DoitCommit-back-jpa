package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;

import java.util.List;

public interface TodoService {

    TodoResDto register(TodoRegDto todoDto, Long principalId);

    void modify(TodoUpdateDto todoUpdateDto);

    void modifyIsFinished(Long todoId);

    void modifyIsFixed(Long todoId);

    void remove(Long todoId);

    List<TodoResDto> getAllTodoList(Long principalId);

    List<TodoResDto> getCustomLimitTodoList(int limit, Long principalId);

    TodoResDto getTodo(Long todoId);

}