package com.web.doitcommit.service.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.dto.board.BoardRegDto;
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
        return result.stream().map(board -> new BoardResDto(board)).collect(Collectors.toList());
    }

    /**
     * 게시글 등록
     */
    @Transactional
    public BoardResDto createBoard(BoardRegDto boardRegDto, Long principalId) {
        Board board = boardRegDto.toEntity(principalId);
        boardRepository.save(board);
        return new BoardResDto(board);
    }

    /**
     * 태그 목록 조회
     */
    @Transactional(readOnly = true)
    public String[] getBoardList() {
        List<String> result = boardRepository.getCustomTagList();
        String[] tagArr = new String[result.size()];
        int size = 0;
        for(String temp : result){
            tagArr[size++] = temp;
        }
        return tagArr;
    }




}
