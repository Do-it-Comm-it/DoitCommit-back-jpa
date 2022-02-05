package com.web.doitcommit.domain.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoRepositoryQuerydsl {

    @Query("select t from Todo t where t.member.memberId = :memberId  order by t.isFixed desc  ")
    List<Todo> getALlTodoList(Long memberId);

}
