package com.web.doitcommit.domain.todo;

import java.util.List;

public interface TodoRepositoryQuerydsl {

    List<Todo> getCustomLimitTodoList(int limit, Long memberId);
}
