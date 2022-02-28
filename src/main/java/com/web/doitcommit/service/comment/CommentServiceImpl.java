package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.comment.CommentRepository;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    @Override
    public Comment createComment(CommentRegDto commentRegDto, Long principalId) {

        Board board = boardRepository.findById(commentRegDto.getBoardId()).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        Comment comment;

        //대댓글 작성일 경우
        if(commentRegDto.getParentId() != null){

            Comment parent = commentRepository.findById(commentRegDto.getParentId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 부모 댓글입니다."));

            comment = commentRegDto.toEntity(board,principalId,parent);
        }
        //댓글 작성일 경우
        else{
            comment = commentRegDto.toEntity(board,principalId);
        }

        return commentRepository.save(comment);
    }
}
