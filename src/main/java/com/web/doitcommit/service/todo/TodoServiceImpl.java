package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoRepository;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    /**
     * 투두 작성
     */
    @Transactional
    @Override
    public TodoResDto register(TodoRegDto todoDto, Long principalId) {

        Todo todo = todoDto.toEntity(principalId);

        todoRepository.save(todo);

        return new TodoResDto(todo);
    }

    /**
     * 투두 수정
     */
    @Transactional
    @Override
    public void modify(TodoUpdateDto todoUpdateDto) {

        Todo todo = todoRepository.findById(todoUpdateDto.getTodoId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않은 투두입니다."));

        //변경감지
        todo.changeTitle(todoUpdateDto.getTitle());
        todo.changeContent(todoUpdateDto.getContent());
        todo.changeType(TodoType.valueOf(todoUpdateDto.getType().toUpperCase()));
        todo.changeImportance(Importance.valueOf(todoUpdateDto.getImportance().toUpperCase()));
        todo.changeIsFixed(todoUpdateDto.getIsFixed());
        todo.changeTodoDateTime(todoUpdateDto.getTodoDateTime());
    }

    /**
     * 투두 완료체크 수정
     */
    @Transactional
    @Override
    public void modifyIsFinished(Long todoId) {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 투두입니다."));

        //완료 전일 경우
        if (todo.getIsFinished() == false){
            todo.changeIsFinished(true);
        }
        //완료일 경우
        else{
            todo.changeIsFinished(false);
        }
    }

    /**
     * 투두 상단고정 수정
     */
    @Transactional
    @Override
    public void modifyIsFixed(Long todoId) {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 투두입니다."));

        //상단 고정일 경우
        if(todo.getIsFixed() == true){
            todo.changeIsFixed(false);
        }
        //상단 고정이 아닐 경우
        else{
            todo.changeIsFixed(true);
        }
    }

    /**
     * 투두 삭제
     */
    @Transactional
    @Override
    public void remove(Long todoId) {

        todoRepository.deleteById(todoId);
    }

    /**
     * 투두 리스트 전체 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<TodoResDto> getAllTodoList(Long principalId) {

        List<Todo> result = todoRepository.getALlTodoList(principalId);

        return result.stream().map(todo -> new TodoResDto(todo)).collect(Collectors.toList());
    }

    /**
     * 투두 리스트 사용자 개수 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<TodoResDto> getCustomLimitTodoList(int limit, Long principalId) {

        List<Todo> result = todoRepository.getCustomLimitTodoList(limit, principalId);

        return result.stream().map(todo -> new TodoResDto(todo)).collect(Collectors.toList());
    }

    /**
     * 투두 단건 조회
     */
    @Transactional(readOnly = true)
    @Override
    public TodoResDto getTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 투두입니다."));

        return new TodoResDto(todo);
    }


}
