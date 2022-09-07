package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.boardCategory.BoardCategoryRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.domain.comment.MemberTag;
import com.web.doitcommit.domain.comment.MemberTagRepository;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.domain.files.MemberImageRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import com.web.doitcommit.dto.comment.CommentListDto;
import com.web.doitcommit.dto.comment.CommentResDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.dto.page.PageRequestDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import com.web.doitcommit.service.image.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
public class CommentServiceImplReadTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberImageRepository memberImageRepository;
    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired MemberTagRepository memberTagRepository;
    @Autowired ImageService imageService;

    private Member member;
    private Board board;

    /**
     * 댓글 수 5
     *
     * -> 게시글 작성자
     * -> 일반회원1(프로필 사진o)
     * -> 게시글 작성자
     * -> 일반회원2(게시글작성자, 일반회원1 태그)
     * -> 게시글 작성자
     */
    @BeforeEach
    void before(){

        //게시글 작성자 생성
        Member member = createMember("boardWriter@naver.com", "boardWriterNickname",
                "boardWriterUsername", "boardWriterOAuthId");
        this.member = member;

        //일반 회원1 생성
        Member createMember1 = createMember("testMember1@naver.com", "test1Nickname", "test1Username",
                "adf111");
        //일반 회원 프로필 사진 생성
        MemberImage createMemberImage = createMemberImage(createMember1, "testFilePath", "testFileNm");

        //일반회원 2 생성
        Member createMember2 = createMember("testMember2@naver.com", "test2Nickname", "test2Username",
                "adf123");

        //게시글 카테고리 생성
        BoardCategory category = createBoardCategory("testName");

        //게시글 생성
        Board board = createBoard(member, category, "testTitle", "testContent");
        this.board = board;

        //게시글 작성자 댓글1
        createComment(member,board, "게시글 작성자의 답변 1");

        //일반 회원1 댓글 생성
        createComment(createMember1, board, "testContent");

        //게시글 작성자 댓글2
        createComment(member,board,"게시글 작성자 답변 2");

        //일반 회원2 댓글 생성
        HashSet<Long> memberIdSet = new HashSet<>();
        memberIdSet.add(member.getMemberId()); //게시글 작성자 태그
        memberIdSet.add(createMember1.getMemberId()); //일반회원1 태그

        createComment(createMember2, board, "태그포함, testContent", memberIdSet);

        //게시글 작성자 댓글3
        createComment(member,board,"게시글 작성자 답변 3");
    }

    @Test
    void 댓글리스트_조회() throws Exception{
        //given

        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(4).build();

        //when
        CommentListDto commentListDto = commentService.getCommentList(board.getBoardId(), pageRequestDto);
        //then
        //댓글 수 검증
        Assertions.assertThat(commentListDto.getTotalCommentCnt()).isEqualTo(5);

        //댓글 리스트 검증
        Assertions.assertThat(commentListDto.getCommentResDtoList().getSize()).isEqualTo(4);
        Assertions.assertThat(commentListDto.getCommentResDtoList().getPage()).isEqualTo(1);
        Assertions.assertThat(commentListDto.getCommentResDtoList().getTotalPage()).isEqualTo(2);
        Assertions.assertThat(commentListDto.getCommentResDtoList().isLast()).isEqualTo(false);

        //회원 태그 리스트 검증
        Assertions.assertThat(commentListDto.getMemberTagResDtoList().size()).isEqualTo(3);

        ScrollResultDto<CommentResDto, Object[]> commentResDtoList = commentListDto.getCommentResDtoList();
        for (CommentResDto dto : commentResDtoList.getDtoList()){
            System.out.println(dto);
        }
        List<MemberTagResDto> memberTagResDtoList = commentListDto.getMemberTagResDtoList();
        for (MemberTagResDto memberTagResDto : memberTagResDtoList){
            System.out.println(memberTagResDto);
        }

    }



    private MemberImage createMemberImage (Member member, String filePath, String testFileNm){

        MemberImage memberImage = new MemberImage(member, imageService.getImage(filePath, testFileNm), false, filePath, testFileNm);
        return memberImageRepository.save(memberImage);
    }

    private Comment createComment (Member member, Board board, String content){

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    private Comment createComment (Member member, Board board, String content, Set<Long> memberIdSet){

        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();

        memberIdSet.forEach(memberId -> comment.addMemberTag(MemberTag.builder()
                .member(Member.builder().memberId(memberId).build())
                .build()));

        return commentRepository.save(comment);
    }

    private Board createBoard (Member member, BoardCategory boardCategory, String title, String
            content){
        Board board = Board.builder()
                .member(member)
                .boardCategory(boardCategory)
                .boardTitle(title)
                .boardContent(content)
                .build();

        return boardRepository.save(board);
    }

    private BoardCategory createBoardCategory (String categoryName){
        BoardCategory category = BoardCategory.builder().categoryName(categoryName).build();

        return boardCategoryRepository.save(category);
    }

    private Member createMember (String email, String nickname, String username, String oAuthId){
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password("1111")
                .username(username)
                .provider(AuthProvider.GOOGLE)
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }
}
