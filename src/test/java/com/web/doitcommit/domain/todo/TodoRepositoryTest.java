package com.web.doitcommit.domain.todo;

import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    void 투두리스트_사용자개수지정() throws Exception{
        //given
        Member member = createMember("member@naver.com", "beforeNickname", "beforeUsername", "beforeOAuthId1");
        Todo todo1 = createTodo(member, "testTitle1", "testContent", TodoType.STUDY, Importance.MEDIUM,
                true, false, LocalDateTime.now());

        Todo todo2 = createTodo(member, "testTitle2", "testContent", TodoType.STUDY, Importance.MEDIUM,
                true, false, LocalDateTime.now().plusDays(1));

        //when
        List<Todo> result = todoRepository.getCustomLimitTodoList(4, member.getMemberId());

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTodoId()).isEqualTo(todo1.getTodoId());
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