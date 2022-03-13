package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.boardCategory.BoardCategoryRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.domain.comment.TagMember;
import com.web.doitcommit.domain.comment.TagMemberRepository;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.domain.files.MemberImageRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.member.TagMemberResDto;
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

@Transactional
@SpringBootTest
class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired TagMemberRepository tagMemberRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired MemberImageRepository memberImageRepository;

    private Member member;
    private Board board;
    private Set<Long> tagMemberIdSet;

    @BeforeEach
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
        System.out.println("-------------------");
        System.out.println("마지막 insert");
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

    /**
     * board 1
     * comment 2 - 게시글 작성자 1(프로필사진x), 일반 회원 1 (프로필사진o)
     */
    @Commit
    @Test
    void 회원태그_리스트_조회() throws Exception{
        //given
        //게시글 작성자 댓글 생성
        createComment(member,board,"testContent");

        //일반 회원 생성
        Member createMember = createMember("testMember@naver.com", "testNickname", "testUsername",
                "adf111");
        //일반 회원 프로필 사진 생성
        MemberImage createMemberImage = createMemberImage(createMember, "testFilePath", "testFileNm");
        //일반 회원 댓글 생성
        createComment(createMember,board,"testContent");


        //when
        List<TagMemberResDto> tagMemberList = commentService.getTagMemberList(board.getBoardId());

        //then
        assertThat(tagMemberList.size()).isEqualTo(2);

        assertThat(tagMemberList.get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(tagMemberList.get(0).getNickname()).isEqualTo(member.getNickname());
        assertThat(tagMemberList.get(0).getImageResDto()).isNull();

        assertThat(tagMemberList.get(1).getMemberId()).isEqualTo(createMember.getMemberId());
        assertThat(tagMemberList.get(1).getNickname()).isEqualTo(createMember.getNickname());
        assertThat(tagMemberList.get(1).getImageResDto().getFilePath()).isEqualTo(createMemberImage.getFilePath());
        assertThat(tagMemberList.get(1).getImageResDto().getFileNm()).isEqualTo(createMemberImage.getFileNm());

        for (TagMemberResDto tagMemberResDto : tagMemberList){
            System.out.println(tagMemberResDto);
        }
    }

    private MemberImage createMemberImage(Member member, String filePath, String testFileNm) {
        MemberImage memberImage = new MemberImage(member, filePath, testFileNm);
        return memberImageRepository.save(memberImage);
    }

    private Comment createComment(Member member, Board board, String content) {
        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
        return commentRepository.save(comment);
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