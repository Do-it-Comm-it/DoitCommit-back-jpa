package com.web.doitcommit.service.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시판 목록 조회
     */
    @Transactional(readOnly = true)
    public List<BoardResDto> getBoardList(int pageNo, int pageSize) {
        List<Board> result = boardRepository.getCustomBoardList(pageNo, pageSize);
        System.out.println("result"+result);
        return result.stream().map(board -> new BoardResDto(board)).collect(Collectors.toList());
        //return result;
    }




}
