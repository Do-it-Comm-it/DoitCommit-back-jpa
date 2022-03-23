package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.domain.comment.MemberTag;
import com.web.doitcommit.domain.comment.MemberTagRepository;
import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.comment.CommentListDto;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentResDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.dto.page.PageRequestDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import com.web.doitcommit.handler.exception.CustomException;
import com.web.doitcommit.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberTagRepository memberTagRepository;
    private final ImageService imageService;

    /**
     * 댓글 작성
     */
    @Transactional
    @Override
    public Comment register(CommentRegDto commentRegDto, Long principalId) {

        Board board = boardRepository.findById(commentRegDto.getBoardId()).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        Comment comment = commentRegDto.toEntity(board,principalId);

        //회원 태그가 있는 경우
        if (commentRegDto.getMemberIdSet() != null && !commentRegDto.getMemberIdSet().isEmpty()){

            commentRegDto.getMemberIdSet().forEach(id ->{
                //memberTag 생성
                MemberTag memberTag = MemberTag.builder()
                        .member(Member.builder().memberId(id).build())
                        .build();

                //memberTag 추가
                comment.addMemberTag(memberTag);
            });
        }

        return commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @Override
    public void modify(CommentUpdateDto commentUpdateDto) {

        Comment comment = commentRepository.findById(commentUpdateDto.getCommentId()).orElseThrow(() ->
                new CustomException("존재하지 않는 댓글입니다."));

        comment.changeContent(commentUpdateDto.getContent());

        updateMemberTag(comment, commentUpdateDto.getMemberIdSet());
    }

    private void updateMemberTag(Comment comment, Set<Long> updateMemberIdSet) {

        //현재 memberTag 모두삭제
        memberTagRepository.deleteByComment(comment);

        //갱신된 memberTag 추가
        addMemberTag(comment, updateMemberIdSet);

    }

    private void addMemberTag(Comment comment, Set<Long> updateMemberIdSet) {

        if(updateMemberIdSet != null && !updateMemberIdSet.isEmpty()){
            for (Long memberId : updateMemberIdSet){
                MemberTag tagMember = MemberTag.builder()
                        .member(Member.builder().memberId(memberId ).build())
                        .comment(comment)
                        .build();

                memberTagRepository.save(tagMember);
            }
        }

    }


    /**
     * 댓글 삭제
     */
    @Transactional
    @Override
    public Long remove(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException("존재하지 않는 댓글입니다."));

        //isExist -> false 로 변경
        comment.remove();

        return comment.getCommentId();
    }

    /**
     * 댓글 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public CommentListDto getCommentList(Long boardId, PageRequestDto pageRequestDto) {

        Page<Object[]> result = commentRepository.getCommentList(boardId, pageRequestDto.getPageable());

        Function<Object[], CommentResDto> fn = (arr -> {
            Image image = (Image) arr[1];

            String imageUrl = null;
            if (image != null){
                imageUrl = imageService.getImage(image.getFilePath(), image.getFileNm());
            }

            return new CommentResDto((Comment) arr[0], imageUrl);
        });

        ScrollResultDto<CommentResDto, Object[]> commentResDtoList = new ScrollResultDto<>(result, fn);

        return new CommentListDto(result.getTotalElements(), commentResDtoList, getMemberTagList(boardId));
    }

    /**
     * 회원 태그 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberTagResDto> getMemberTagList(Long boardId) {
        List<Object[]> result = commentRepository.getMemberTagList(boardId);

        return result.stream().map(arr -> {
            Image image = (Image) arr[2];

            String imageUrl = null;

            if(image != null){
                imageUrl = imageService.getImage(image.getFilePath(), image.getFileNm());
            }

            return new MemberTagResDto((Long) arr[0], (String) arr[1], imageUrl);
        }).collect(Collectors.toList());
    }
}
