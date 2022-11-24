package com.web.doitcommit.service.bookmark;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.domain.bookmark.Bookmark;
import com.web.doitcommit.domain.bookmark.BookmarkRepository;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;

    /**
     * 북마크 생성
     */
    @Transactional
    @Override
    public Bookmark createBookmark(Long principalId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        Bookmark bookmark = Bookmark.builder().member(Member.builder().memberId(principalId).build()).build();
        bookmark.setBoard(board);

        return bookmarkRepository.save(bookmark);
    }

    /**
     * 북마크 취소
     */
    @Transactional
    @Override
    public void cancelBookmark(Long principalId, Long boardId) {

        bookmarkRepository.deleteBookmark(principalId,boardId);
    }

    /**
     * 북마크 다중 취소
     */
    @Transactional
    @Override
    public void cancelMultipleBookmark(Long principalId,  List<Long> boardIdList) {

        bookmarkRepository.deleteMultipleBookmark(principalId, boardIdList);
    }
}
