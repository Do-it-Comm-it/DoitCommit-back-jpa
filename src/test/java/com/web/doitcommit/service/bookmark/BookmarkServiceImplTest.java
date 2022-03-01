package com.web.doitcommit.service.bookmark;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.boardCategory.BoardCategoryRepository;
import com.web.doitcommit.domain.bookmark.Bookmark;
import com.web.doitcommit.domain.bookmark.BookmarkRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
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
class BookmarkServiceImplTest {
    @Autowired BookmarkService bookmarkService;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired BookmarkRepository bookmarkRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void before(){
        Member member = createMember("testNickname", "testUsername", "adl231");
        this.member = member;
        BoardCategory category = createBoardCategory("testName");

        //게시글 태그 set 생성
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Spring");
        tagSet.add("개발자");
        //게시글 생성
        Board board = createBoard(member, category, "testTitle", "testContent", tagSet);
        this.board = board;
    }

    /**
     * 북마크 추가
     */
    @Test
    void 북마크_추가() throws Exception{
        //given
        //when
        Bookmark bookmark = bookmarkService.createBookmark(member.getMemberId(), board.getBoardId());

        //then
        Bookmark findBookmark = bookmarkRepository.findById(bookmark.getBookmarkId()).get();
        assertThat(findBookmark.getBookmarkId()).isNotNull();
        assertThat(findBookmark.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findBookmark.getBoard().getBoardId()).isEqualTo(board.getBoardId());
    }

    /**
     * 좋아요 취소
     */
    @Test
    void 좋아요_취소() throws Exception{
        //given
        Bookmark bookmark = createBookmark(member, board);

        //when
        bookmarkService.cancelBookmark(member.getMemberId(),board.getBoardId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> bookmarkRepository.findById(bookmark.getBookmarkId()).get());

        assertThat(e.getMessage()).isEqualTo("No value present");
    }

    private Bookmark createBookmark(Member member, Board board) {
        Bookmark bookmark = Bookmark.builder().member(member).build();
        bookmark.setBoard(board);

        return bookmarkRepository.save(bookmark);
    }

    private Board createBoard(Member member, BoardCategory boardCategory, String title,
                              String content, Set<String> tagSet) {
        Board board = Board.builder()
                .member(member)
                .boardCategory(boardCategory)
                .boardTitle(title)
                .boardContent(content)
                .tag(tagSet)
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