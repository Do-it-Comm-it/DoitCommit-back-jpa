package com.web.doitcommit.service.boardHistory;

import com.web.doitcommit.domain.boardHistory.BoardHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardHistoryServiceImpl implements BoardHistoryService{

    private final BoardHistoryRepository boardHistoryRepository;

    /**
     * 게시글 히스토리 삭제
     */
    @Transactional
    @Override
    public void remove(Long boardId, Long memberId) {

        boardHistoryRepository.deleteBoardHistory(boardId, memberId);

    }
}
