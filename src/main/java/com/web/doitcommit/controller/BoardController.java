package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.board.BoardResDto;
import com.web.doitcommit.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 조회
     */
    @GetMapping("/list")
    public ResponseEntity<?> getBoardList(){
        List<BoardResDto> boardResDtoList = boardService.getBoardList();
        return new ResponseEntity<>(new CMRespDto<>(1, "메인화면 투두리스트 조회 성공", boardResDtoList),HttpStatus.OK);
    }


}
