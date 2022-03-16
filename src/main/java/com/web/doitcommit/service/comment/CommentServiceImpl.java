package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.domain.comment.MemberTag;
import com.web.doitcommit.domain.comment.MemberTagRepository;
import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberTagRepository memberTagRepository;

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

        Set<MemberTag> findTagMember = comment.getMemberTagSet();

        Set<Long> findMemberIdSetOfTagMember = findTagMember.stream().map(tagMember -> {
            return tagMember.getMember().getMemberId();
        }).collect(Collectors.toSet());

        Set<Long> memberIdSet = commentUpdateDto.getMemberIdSet();

        Set<Long> memberIdSet2 = memberIdSet;

        if(memberIdSet != null && !memberIdSet.isEmpty()){
            memberIdSet.remove(findMemberIdSetOfTagMember);
        }

        if(memberIdSet2 != null && !memberIdSet2.isEmpty()){
            findMemberIdSetOfTagMember.retainAll(memberIdSet2);
        }


        //신규 태그 추가
        if(memberIdSet != null && !memberIdSet.isEmpty()){
            for (Long id : memberIdSet){
                MemberTag tagMember = MemberTag.builder()
                        .member(Member.builder().memberId(id).build())
                        .build();
                comment.addMemberTag(tagMember);
            }
        }

        //삭제된 태그 tagMember 삭제
//        comment.getTagMemberSet().forEach(tagMember -> {
//            for (Long id : findMemberIdSetOfTagMember){
//                if (tagMember.getMember().getMemberId().equals(id)){
//                    comment.getTagMemberSet().remove(tagMember);
//                    break;
//                }
//            }
//        });
        if (!findMemberIdSetOfTagMember.isEmpty()) {
            memberTagRepository.deleteAllByMemberIdInQuery(findMemberIdSetOfTagMember);
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
     * 회원 태그 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberTagResDto> getMemberTagList(Long boardId) {
        List<Object[]> result = commentRepository.getMemberTagList(boardId);

        List<MemberTagResDto> memberTagResDtoList = result.stream().map(arr ->
                new MemberTagResDto((Long) arr[0], (String) arr[1], (Image) arr[2]))
                .collect(Collectors.toList());

        return memberTagResDtoList;
    }
}
