package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoRepository;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.todo.TodoRegDto;
import com.web.doitcommit.dto.todo.TodoResDto;
import com.web.doitcommit.dto.todo.TodoUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoServiceImplTest {
    
    @Autowired TodoService todoService;
    @Autowired TodoRepository todoRepository;
    @Autowired MemberRepository memberRepository;

    Long memberId;
    Long todoId;

    @BeforeEach
    void before(){
        Member member = createMember("before@naver.com", "beforeNickname", "beforeUsername", "beforeOAuthId");
        this.memberId = member.getMemberId();
        Todo todo = createTodo(member, "testTitle", "testContent", TodoType.STUDY, Importance.MEDIUM, false, false, LocalDateTime.now());
        this.todoId = todo.getTodoId();
    }


    @Test
    void 투두생성_todoDateTime_기입o() throws Exception{
        //given
        Member member = createMember("testEmail@naver.com", "testNickname", "testUsername", "testOAuthId");
        TodoRegDto todoRegDto = createTodoRegDto("testTitle", "testContent", "STUDY", "Low", false, LocalDateTime.now());

        //when
        TodoResDto todoResDto = todoService.register(todoRegDto, member.getMemberId());

        //then
        Todo findTodo = todoRepository.findById(todoResDto.getTodoId()).get();

        //투두 검증
        assertThat(findTodo.getTodoId()).isNotNull();
        assertThat(findTodo.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findTodo.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(findTodo.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(findTodo.getType()).isEqualTo(TodoType.STUDY);
        assertThat(findTodo.getImportance()).isEqualTo(Importance.valueOf(todoRegDto.getImportance().toUpperCase()));
        assertThat(findTodo.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(findTodo.getIsFinished()).isEqualTo(false);
        assertThat(findTodo.getTodoDateTime()).isEqualTo(todoRegDto.getTodoDateTime());

        //todoResDto 검증
        assertThat(todoResDto).isNotNull();
        assertThat(todoResDto.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(todoResDto.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(todoResDto.getType()).isEqualTo(todoResDto.getType().toUpperCase());
        assertThat(todoResDto.getImportance()).isEqualTo(todoRegDto.getImportance().toUpperCase());
        assertThat(todoResDto.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(todoResDto.getIsFinished()).isEqualTo(false);
        assertThat(todoResDto.getTodoDateTime()).isEqualTo(todoRegDto.getTodoDateTime());
    }


    @Test
    void 투두생성_todoDateTime_기입x() throws Exception{
        //given
        Member member = createMember("testEmail", "testNickname", "testUsername", "testOAuthId");
        TodoRegDto todoRegDto = createTodoRegDto("testTitle", "testContent", "STUDY", "Low", false);

        //when
        TodoResDto todoResDto = todoService.register(todoRegDto, member.getMemberId());

        //then
        Todo findTodo = todoRepository.findById(todoResDto.getTodoId()).get();

        //투두 검증
        assertThat(findTodo.getTodoId()).isNotNull();
        assertThat(findTodo.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findTodo.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(findTodo.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(findTodo.getType()).isEqualTo(TodoType.STUDY);
        assertThat(findTodo.getImportance()).isEqualTo(Importance.valueOf(todoRegDto.getImportance().toUpperCase()));
        assertThat(findTodo.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(findTodo.getIsFinished()).isEqualTo(false);
        assertThat(findTodo.getTodoDateTime().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());

        //todoResDto 검증
        assertThat(todoResDto).isNotNull();
        assertThat(todoResDto.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(todoResDto.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(todoResDto.getType()).isEqualTo(todoResDto.getType().toUpperCase());
        assertThat(todoResDto.getImportance()).isEqualTo(todoRegDto.getImportance().toUpperCase());
        assertThat(todoResDto.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(todoResDto.getIsFinished()).isEqualTo(false);
        assertThat(todoResDto.getTodoDateTime().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
    }

    @Test
    void 투두_수정() throws Exception{
        //given
        Todo testTodo = todoRepository.findById(todoId).get();

        TodoUpdateDto todoUpdateDto = createTodoUpdateDto(testTodo.getTodoId(), "updateTitle", "updateContent", "study",
                "high", true, LocalDateTime.of(2022, 01, 27, 13, 00));

        //when
        todoService.modify(todoUpdateDto);

        //then
        Todo findTodo = todoRepository.findById(testTodo.getTodoId()).get();

        assertThat(findTodo.getTodoId()).isEqualTo(testTodo.getTodoId());
        assertThat(findTodo.getMember().getMemberId()).isEqualTo(memberId);
        assertThat(findTodo.getTitle()).isEqualTo(todoUpdateDto.getTitle());
        assertThat(findTodo.getContent()).isEqualTo(todoUpdateDto.getContent());
        assertThat(findTodo.getType()).isEqualTo(TodoType.valueOf(todoUpdateDto.getType().toUpperCase()));
        assertThat(findTodo.getImportance()).isEqualTo(Importance.valueOf(todoUpdateDto.getImportance().toUpperCase()));
        assertThat(findTodo.getIsFixed()).isEqualTo(todoUpdateDto.getIsFixed());
        assertThat(findTodo.getIsFinished()).isEqualTo(testTodo.getIsFinished());
        assertThat(findTodo.getTodoDateTime()).isEqualTo(todoUpdateDto.getTodoDateTime());
    }

    /**
     * 기존 투두 isFinished - false
     */
    @Test
    void 투두_완료상태_수정() throws Exception{
        //given
        //when
        todoService.modifyIsFinished(todoId);

        //then
        Todo findTodo = todoRepository.findById(todoId).get();
        assertThat(findTodo.getIsFinished()).isTrue();
    }

    /**
     * 기존 투두 isFixed - false
     */
    @Test
    void 투두_상단고정_수정() throws Exception{
        //given

        //when
        todoService.modifyIsFixed(todoId);

        //then
        Todo findTodo = todoRepository.findById(todoId).get();
        assertThat(findTodo.getIsFixed()).isTrue();
    }

    @Test
    void 투두_삭제() throws Exception{
        //given
        //when
        todoService.remove(todoId);

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> (todoRepository.findById(todoId)).get());

        assertThat(e.getMessage()).isEqualTo("No value present");
    }

    @Test
    void 투두단건_조회() throws Exception{
        //given
        //when
        TodoResDto todoResDto = todoService.getTodo(todoId);

        //then
        Todo findTodo = todoRepository.findById(todoId).get();

        assertThat(todoResDto.getTodoId()).isEqualTo(findTodo.getTodoId());
        assertThat(todoResDto.getTitle()).isEqualTo(findTodo.getTitle());
        assertThat(todoResDto.getContent()).isEqualTo(findTodo.getContent());
        assertThat(todoResDto.getType()).isEqualTo(findTodo.getType().toString());
        assertThat(todoResDto.getImportance()).isEqualTo(findTodo.getImportance().toString());
        assertThat(todoResDto.getIsFixed()).isEqualTo(findTodo.getIsFixed());
        assertThat(todoResDto.getIsFinished()).isEqualTo(findTodo.getIsFinished());
        assertThat(todoResDto.getTodoDateTime()).isEqualTo(findTodo.getTodoDateTime());
    }

    private TodoUpdateDto createTodoUpdateDto(Long todoId, String title, String content, String type,
                                              String importance, Boolean isFixed, LocalDateTime todoDateTime) {
        return TodoUpdateDto.builder()
                .todoId(todoId)
                .title(title)
                .content(content)
                .type(type)
                .importance(importance)
                .isFixed(isFixed)
                .todoDateTime(todoDateTime)
                .build();
    }

    private Todo createTodo(Member member, String title, String content, TodoType type, Importance importance,
                            Boolean isFixed, Boolean isFinished, LocalDateTime todoDateTime) {

        Todo todo = Todo.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .importance(importance)
                .isFixed(isFixed)
                .isFinished(isFinished)
                .todoDateTime(todoDateTime)
                .build();

        return todoRepository.save(todo);
    }

    private TodoRegDto createTodoRegDto(String title, String content, String type, String importance,
                                        Boolean isFixed) {
        return TodoRegDto.builder()
                .title(title)
                .content(content)
                .type(type)
                .importance(importance)
                .isFixed(isFixed)
                .build();
    }

    private TodoRegDto createTodoRegDto(String title, String content, String type, String importance,
                                        Boolean isFixed, LocalDateTime todoDateTime) {
        return TodoRegDto.builder()
                .title(title)
                .content(content)
                .type(type)
                .importance(importance)
                .isFixed(isFixed)
                .todoDateTime(todoDateTime)
                .build();
    }


    private Member createMember(String email, String nickname, String username, String oAuthId) {
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password("1111")
                .username(username)
                .provider(AuthProvider.GOOGLE)
                .interestTechSet(new HashSet<>(Arrays.asList("java")))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }


}