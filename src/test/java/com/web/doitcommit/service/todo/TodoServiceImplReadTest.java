package com.web.doitcommit.service.todo;

import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.domain.todo.Importance;
import com.web.doitcommit.domain.todo.Todo;
import com.web.doitcommit.domain.todo.TodoRepository;
import com.web.doitcommit.domain.todo.TodoType;
import com.web.doitcommit.dto.todo.TodoResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class TodoServiceImplReadTest {

    @Autowired TodoService todoService;
    @Autowired TodoRepository todoRepository;
    @Autowired MemberRepository memberRepository;

    private Long principalId;

    /**
     * member1
     * todo1 - 상단고정 false, 난이도 medium, 완료상태 - true,  현재날짜 o
     * todo2 - 상단고정 true,  난이도 medium, 완료상태 - true,  현재날짜 o
     * todo3 - 상단고정 false, 난이도 medium, 완료상태 - true, 현재날짜 o
     * todo4 - 상단고정 true,  난이도 high,   완료상태 - false, 현재날짜 o
     * todo5 - 상단고정 true,  난이도 low,    완료상태 - false, 현재날짜 o
     * todo6 - 상단고정 true,  난이도 high,   완료상태 - false, 현재날짜 x
     *
     * member2
     * todo7 - 상단고정 true,  난이도 medium, 완료상태 - true,  현재날짜 o
     */
    @BeforeEach
    void before(){
        Member member1 = createMember("member1@naver.com", "beforeNickname1", "beforeUsername1", "beforeOAuthId1");
        principalId = member1.getMemberId();
        Member member2 = createMember("member2@naver.com", "beforeNickname2", "beforeUsername2", "beforeOAuthId2");

        Todo todo1 = createTodo(member1, "title1", "content", TodoType.STUDY, Importance.MEDIUM, false,
                false, LocalDateTime.now());

        Todo todo2 = createTodo(member1, "title2", "content", TodoType.STUDY, Importance.MEDIUM, true,
                true, LocalDateTime.now());

        Todo todo3 = createTodo(member1, "title3", "content", TodoType.STUDY, Importance.MEDIUM, false,
                false, LocalDateTime.now());

        Todo todo4 = createTodo(member1, "title4", "content", TodoType.STUDY, Importance.HIGH, true,
                false, LocalDateTime.now());

        Todo todo5 = createTodo(member1, "title5", "content", TodoType.STUDY, Importance.LOW, true,
                false, LocalDateTime.now());

        Todo todo6 = createTodo(member1, "title6", "content", TodoType.STUDY, Importance.HIGH, true,
                false, LocalDateTime.now().plusDays(1));

        Todo todo7 = createTodo(member2, "title7", "content", TodoType.STUDY, Importance.MEDIUM, true,
                true, LocalDateTime.now());
    }


    /**
     * member1
     * todo1 - 상단고정 false, 난이도 medium, 완료상태 - false,  현재날짜 o
     * todo2 - 상단고정 true,  난이도 medium, 완료상태 - true,  현재날짜 o
     * todo3 - 상단고정 false, 난이도 medium, 완료상태 - true, 현재날짜 o
     * todo4 - 상단고정 true,  난이도 high,   완료상태 - false, 현재날짜 o
     * todo5 - 상단고정 true,  난이도 low,    완료상태 - false, 현재날짜 o
     * todo6 - 상단고정 true,  난이도 high,   완료상태 - false, 현재날짜 x
     *
     * member2
     * todo7 - 상단고정 true,  난이도 medium, 완료상태 - true,  현재날짜 o
     *
     * 조회 member1, limit 4
     * -> todo4, todo5, todo2, todo1 순으로 출력
     */
    @Test
    void 투두리스트_사용자_개수지정() throws Exception{
        //given

        //when
        List<TodoResDto> result = todoService.getCustomLimitTodoList(4, principalId);

        //then
        for (TodoResDto dto : result){
            System.out.println(dto);
        }

        assertThat(result).extracting("title")
                .containsExactly("title4","title5","title2","title1");
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
                .interestTechSet(new HashSet<>(Arrays.asList("Java")))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }
}
