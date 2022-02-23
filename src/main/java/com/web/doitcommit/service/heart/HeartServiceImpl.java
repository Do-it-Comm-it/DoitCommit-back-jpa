package com.web.doitcommit.service.heart;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.heart.Heart;
import com.web.doitcommit.domain.heart.HeartRepository;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class HeartServiceImpl implements HeartService{

    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;

    /**
     * 좋아요
     */
    @Transactional
    @Override
    public Heart heart(Long principalId, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        Heart heart = Heart.builder().member(Member.builder().memberId(principalId).build()).build();
        heart.setBoard(board);

        return heartRepository.save(heart);
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    @Override
    public void cancelHeart(Long principalId, Long boardId) {

        heartRepository.deleteHeart(principalId,boardId);

    }
}
