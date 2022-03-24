package com.web.doitcommit.service.heart;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.boardCategory.BoardCategoryRepository;
import com.web.doitcommit.domain.heart.Heart;
import com.web.doitcommit.domain.heart.HeartRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class HeartServiceImplTest {

    @Autowired HeartService heartService;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired HeartRepository heartRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void before(){
        Member member = createMember("testNickname", "testUsername", "adl231");
        this.member = member;
        BoardCategory category = createBoardCategory("testName");

        //게시글 생성
        Board board = createBoard(member, category, "testTitle", "testContent");
        this.board = board;
    }

    /**
     * 좋아요
     */
    @Test
    void 좋아요_추가() throws Exception{
        //given
        //when
        Heart heart = heartService.heart(member.getMemberId(), board.getBoardId());

        //then
        Heart findHeart = heartRepository.findById(heart.getHeartId()).get();
        assertThat(findHeart.getHeartId()).isNotNull();
        assertThat(findHeart.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findHeart.getBoard().getBoardId()).isEqualTo(board.getBoardId());
    }

    /**
     * 좋아요 취소
     */
    @Test
    void 좋아요_취소() throws Exception{
        //given
        Heart heart = createHeart(member, board);
        //when
        heartService.cancelHeart(member.getMemberId(),board.getBoardId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> heartRepository.findById(heart.getHeartId()).get());

        assertThat(e.getMessage()).isEqualTo("No value present");
    }

    private Heart createHeart(Member member, Board board) {
        Heart heart = Heart.builder().member(member).build();
        heart.setBoard(board);
        heartRepository.save(heart);
        return heart;
    }

    private Board createBoard(Member member, BoardCategory boardCategory, String title,
                              String content) {
        Board board = Board.builder()
                .member(member)
                .boardCategory(boardCategory)
                .boardTitle(title)
                .boardContent(content)
                .build();

        return boardRepository.save(board);
    }

    private BoardCategory createBoardCategory(String categoryName) {
        BoardCategory category = BoardCategory.builder().categoryName(categoryName).build();

        return boardCategoryRepository.save(category);
    }

    public Member createMember(String nickname, String username,
                               String oauthId){
        Member member = Member.builder()
                .nickname(nickname)
                .username(username)
                .password("1111")
                .provider(AuthProvider.GOOGLE)
                .oauthId(oauthId)
                .role("USER")
                .build();

        return memberRepository.save(member);
    }

}