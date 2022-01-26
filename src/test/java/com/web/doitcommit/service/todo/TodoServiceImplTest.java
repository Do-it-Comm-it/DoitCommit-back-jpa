package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoRepository;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.todo.TodoRegDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoServiceImplTest {
    
    @Autowired TodoService todoService;
    @Autowired TodoRepository todoRepository;
    @Autowired MemberRepository memberRepository;
    
    @Test
    void 투두생성_todoDateTime_기입o() throws Exception{
        //given
        Member member = createMember("testEmail", "testNickname", "testUsername", "testOAuthId");
        TodoRegDto todoRegDto = createTodoRegDto("testTitle", "tetsContent", "STUDY", "Low", false, LocalDateTime.now());

        //when
        Todo todo = todoService.register(todoRegDto, member.getMemberId());

        //then
        assertThat(todo.getTodoId()).isNotNull();
        assertThat(todo.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(todo.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(todo.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(todo.getType()).isEqualTo(TodoType.STUDY);
        assertThat(todo.getImportance()).isEqualTo(Importance.valueOf(todoRegDto.getImportance().toUpperCase()));
        assertThat(todo.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(todo.getIsFinished()).isEqualTo(false);
        assertThat(todo.getTodoDateTime()).isEqualTo(todoRegDto.getTodoDateTime());
    }


    @Test
    void 투두생성_todoDateTime_기입x() throws Exception{
        //given
        Member member = createMember("testEmail", "testNickname", "testUsername", "testOAuthId");
        TodoRegDto todoRegDto = createTodoRegDto("testTitle", "testContent", "STUDY", "Low", false);

        //when
        Todo todo = todoService.register(todoRegDto, member.getMemberId());

        //then
        assertThat(todo.getTodoId()).isNotNull();
        assertThat(todo.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(todo.getTitle()).isEqualTo(todoRegDto.getTitle());
        assertThat(todo.getContent()).isEqualTo(todoRegDto.getContent());
        assertThat(todo.getType()).isEqualTo(TodoType.STUDY);
        assertThat(todo.getImportance()).isEqualTo(Importance.valueOf(todoRegDto.getImportance().toUpperCase()));
        assertThat(todo.getIsFixed()).isEqualTo(todoRegDto.getIsFixed());
        assertThat(todo.getIsFinished()).isEqualTo(false);
        assertThat(todo.getTodoDateTime().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
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
                .interestTechSet(new HashSet<>(Arrays.asList(InterestTech.Java)))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }


}