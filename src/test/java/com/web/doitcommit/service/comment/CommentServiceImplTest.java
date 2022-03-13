package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.boardCategory.BoardCategoryRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.domain.comment.TagMember;
import com.web.doitcommit.domain.comment.TagMemberRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired TagMemberRepository tagMemberRepository;
    @Autowired CommentRepository commentRepository;

    private Member member;
    private Board board;
    private Set<Long> tagMemberIdSet;

    //@BeforeEach
    void before(){
        Member member = createMember("before@naver.com", "beforeNickname", "beforeUsername", "beforeOAuthId");
        this.member = member;

        BoardCategory category = createBoardCategory("testName");

        //게시글 태그 set 생성
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Spring");
        tagSet.add("개발자");

        //게시글 생성
        Board board = createBoard(member, category, "testTitle", "testContent", tagSet);
        this.board = board;

        //회원 태그 리스트
        Set<Long> tagMemberIdSet = new HashSet<>();

        IntStream.rangeClosed(1,3).forEach(i -> {
            Member newMember = createMember("before" + i + "@naver.com", "beforeNickname" + i,
                    "beforeUsername" + i, "beforeOAuthId" + i);

            tagMemberIdSet.add(memberRepository.save(newMember).getMemberId());
        });

        this.tagMemberIdSet = tagMemberIdSet;
    }


    @Test
    void 댓글작성() throws Exception{
        //given

        CommentRegDto commentRegDto =
                createCommentRegDto(board.getBoardId(), "testContent", tagMemberIdSet);

        //when
        Comment comment = commentService.register(commentRegDto, member.getMemberId());

        //then
        assertThat(comment.getCommentId()).isNotNull();
        assertThat(comment.getBoard().getBoardId()).isEqualTo(board.getBoardId());
        assertThat(comment.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(comment.getContent()).isEqualTo(commentRegDto.getContent());
        assertThat(comment.getIsExist()).isTrue();

        //tagMember
        Set<TagMember> findTagMemberSet = tagMemberRepository.findByComment(comment);
      
        assertThat(comment.getTagMemberSet()).isEqualTo(findTagMemberSet);
    }

    @Commit
    @Test
    void 댓글수정() throws Exception{
        //given
        Set<TagMember> tagMemberSet = new HashSet<>();
/*
        tagMemberIdSet.forEach(id ->{
            //tagMember 생성
            TagMember tagMember = TagMember.builder()
                    .member(Member.builder().memberId(id).build())
                    .build();

            //tagMember 추가
            tagMemberSet.add(tagMember);
        });


        Comment comment = createComment(member, board, "testContent", tagMemberSet);*/

        CommentUpdateDto commentUpdateDto =
                createUpdateDto(1L, "updateContent", null);
        //when
        commentService.modify(commentUpdateDto);

        //then
        Comment findComment = commentRepository.findById(1L).get();
        Set<TagMember> result = tagMemberRepository.findByComment(findComment);
        for (TagMember tagMember : result){
            System.out.println(tagMember);
        }

        System.out.println(findComment);
    }

    private Comment createComment(Member member, Board board, String content, Set<TagMember> tagMemberSet) {

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();

        tagMemberSet.forEach(tagMember -> comment.addTagMember(tagMember));

        return commentRepository.save(comment);
    }

    private CommentUpdateDto createUpdateDto(Long commentId, String content, Set<Long> tagMemberIdSet) {

        return CommentUpdateDto.builder()
                .commentId(commentId)
                .content(content)
                .memberIdSet(tagMemberIdSet)
                .build();
    }

    private CommentRegDto createCommentRegDto(Long boardId, String content, Set<Long> tagMemberIdSet) {
        return CommentRegDto.builder()
                .boardId(boardId)
                .content(content)
                .memberIdSet(tagMemberIdSet)
                .build();
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