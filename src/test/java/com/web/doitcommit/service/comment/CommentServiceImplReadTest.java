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
import com.web.doitcommit.dto.page.ScrollResultDto;
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

    private Member member;
    private Board board;

    @BeforeEach
    void before(){
        Member member = createMember("boardWriter@naver.com", "boardWriterNickname",
                "boardWriterUsername", "boardWriterOAuthId");
        this.member = member;

        BoardCategory category = createBoardCategory("testName");

        //게시글 태그 set 생성
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Spring");
        tagSet.add("개발자");

        //게시글 생성
        Board board = createBoard(member, category, "testTitle", "testContent", tagSet);
        this.board = board;

        //게시글 작성자 댓글1
        createComment(member,board, "게시글 작성자의 답변 1");

        //일반 회원 생성
        Member createMember = createMember("testMember@naver.com", "testNickname", "testUsername",
                "adf111");
        //일반 회원 프로필 사진 생성
        MemberImage createMemberImage = createMemberImage(createMember, "testFilePath", "testFileNm");

        //일반 회원 댓글 생성
        HashSet<Long> memberIdSet = new HashSet<>();
        memberIdSet.add(member.getMemberId());
        createComment(createMember, board, "testContent", memberIdSet);

        //게시글 작성자 댓글2
        createComment(member,board,"게시글 작성자 답젼 2");
    }

    @Test
    void 댓글리스트_조회() throws Exception{
        //given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("commentId").ascending());

        //when
        CommentListDto commentListDto = commentService.getCommentList(board.getBoardId(), pageable);

        //then
        System.out.println(commentListDto.getCommentCount());
        System.out.println(commentListDto.getCommentResDtoList());
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
        MemberImage memberImage = new MemberImage(member, filePath, testFileNm);
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
            content, Set< String > tagSet){
        Board board = Board.builder()
                .member(member)
                .boardCategory(boardCategory)
                .boardTitle(title)
                .boardContent(content)
                .tag(tagSet)
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
                .interestTechSet(new HashSet<>(Arrays.asList("java")))
                .oauthId(oAuthId)
                .build();

        return memberRepository.save(member);
    }
}
