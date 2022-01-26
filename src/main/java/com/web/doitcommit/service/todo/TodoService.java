package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;

public interface TodoService {

    Todo register(TodoRegDto todoDto, Long principalId);

    void modify(TodoUpdateDto todoUpdateDto);

    void modifyIsFinished(Long todoId);

    void modifyIsFixed(Long todoId);

    void remove(Long todoId);

    //TODO 조회 관련

}
